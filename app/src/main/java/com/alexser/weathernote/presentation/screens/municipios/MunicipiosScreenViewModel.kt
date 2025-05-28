package com.alexser.weathernote.presentation.screens.municipios

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexser.weathernote.data.firebase.MunicipioSyncService
import com.alexser.weathernote.data.local.HomeMunicipioPreferences
import com.alexser.weathernote.data.local.SnapshotPreferences
import com.alexser.weathernote.data.remote.mapper.toHourlyForecastFullItems
import com.alexser.weathernote.data.remote.model.HourlyForecastFullItem
import com.alexser.weathernote.domain.model.DailyForecast
import com.alexser.weathernote.domain.model.SavedMunicipio
import com.alexser.weathernote.domain.model.SnapshotReport
import com.alexser.weathernote.domain.usecase.*
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.InputStreamReader
import javax.inject.Inject

@HiltViewModel
class MunicipiosScreenViewModel @Inject constructor(
    private val getSavedMunicipiosUseCase: GetSavedMunicipiosUseCase,
    private val addMunicipioUseCase: AddMunicipioUseCase,
    private val removeMunicipioUseCase: RemoveMunicipioUseCase,
    private val getBasicWeatherForecastUseCase: GetBasicWeatherForecastUseCase,
    private val findMunicipioByNameUseCase: FindMunicipioByNameUseCase,
    private val syncService: MunicipioSyncService,
    private val getHourlyForecastUseCase: GetHourlyForecastUseCase,
    private val homeMunicipioPreferences: HomeMunicipioPreferences,
    private val deleteSnapshotsByMunicipioUseCase: DeleteSnapshotsByMunicipioUseCase,
    private val snapshotPreferences: SnapshotPreferences,
    private val getDailyForecastUseCase: GetDailyForecastUseCase,
    private val getSnapshotByReportIdUseCase: GetSnapshotByReportIdUseCase,
    private val saveSnapshotReportUseCase: SaveSnapshotReportUseCase
    ) : ViewModel() {

    private val _municipios = MutableStateFlow<List<SavedMunicipio>>(emptyList())
    val municipios: StateFlow<List<SavedMunicipio>> = _municipios

    private val _snapshotMunicipioUiStates = MutableStateFlow<Map<String, SnapshotMunicipioUiState>>(emptyMap())
    val snapshotMunicipioUiStates: StateFlow<Map<String, SnapshotMunicipioUiState>> = _snapshotMunicipioUiStates

    private val _syncSuccess = MutableStateFlow<Boolean?>(null)
    val syncSuccess: StateFlow<Boolean?> = _syncSuccess

    private val _suggestions = MutableStateFlow<List<String>>(emptyList())
    val suggestions: StateFlow<List<String>> get() = _suggestions

    private val _hourlyFullForecasts = MutableStateFlow<Map<String, List<HourlyForecastFullItem>>>(emptyMap())
    val hourlyFullForecasts: StateFlow<Map<String, List<HourlyForecastFullItem>>> = _hourlyFullForecasts

    val homeMunicipioId = homeMunicipioPreferences.homeMunicipioId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage

    private val _homeConfirmationMessage = MutableStateFlow<String?>(null)
    val homeConfirmationMessage: StateFlow<String?> = _homeConfirmationMessage

    init {
        loadMunicipiosAndSnapshots()
    }

    private fun loadMunicipiosAndSnapshots() {
        viewModelScope.launch {
            try {
                val remoteMunicipios = syncService.downloadRemoteMunicipios()
                val current = getSavedMunicipiosUseCase().first()
                val currentIds = current.map { it.id }.toSet()

                remoteMunicipios.forEach { municipio ->
                    if (municipio.id !in currentIds) {
                        addMunicipioUseCase(municipio)
                    }
                }
            } catch (e: Exception) {
                _syncSuccess.value = false
            }

            getSavedMunicipiosUseCase().collect { savedList ->
                _municipios.value = savedList
                savedList.forEach { municipio ->
                    if (!_snapshotMunicipioUiStates.value.containsKey(municipio.id)) {
                        fetchSnapshot(municipio.id)
                    }
                }
            }
        }
    }

    fun fetchSnapshot(municipioId: String) {
        viewModelScope.launch {
            _snapshotMunicipioUiStates.update { it + (municipioId to SnapshotMunicipioUiState.Loading) }

            try {
                val snapshot = retryIO { getBasicWeatherForecastUseCase(municipioId).getOrThrow() }
                _snapshotMunicipioUiStates.update { it + (municipioId to SnapshotMunicipioUiState.Success(snapshot)) }
            } catch (e: Exception) {
                _snapshotMunicipioUiStates.update { it + (municipioId to SnapshotMunicipioUiState.Error(e.message ?: "Error al cargar datos")) }
            }
        }
    }

    fun addMunicipioByName(name: String) {
        viewModelScope.launch {
            val id = findMunicipioByNameUseCase(name)
            if (id != null && _municipios.value.none { it.id == id }) {
                val newMunicipio = SavedMunicipio(id = id, nombre = name)
                addMunicipioUseCase(newMunicipio)
                fetchSnapshot(id)
                syncMunicipiosToFirestore()
            }
        }
    }

    fun removeMunicipioWithOption(id: String, deleteSnapshots: Boolean) {
        viewModelScope.launch {
            val toRemove = _municipios.value.find { it.id == id } ?: return@launch
            removeMunicipioUseCase(toRemove)

            if (deleteSnapshots) {
                deleteSnapshotsByMunicipioUseCase(toRemove.id)
                snapshotPreferences.removeRetentionForMunicipio(toRemove.id)
            }

            _municipios.update { it.filterNot { it.id == id } }
            _snapshotMunicipioUiStates.update { it - id }

            val msg = if (deleteSnapshots) {
                "Municipio and snapshots deleted"
            } else {
                "Municipio deleted (snapshots kept)"
            }

            _snackbarMessage.value = msg
            syncMunicipiosToFirestore()
        }
    }

    fun setHomeMunicipio(id: String) {
        viewModelScope.launch {
            homeMunicipioPreferences.setHomeMunicipioId(id)
            val name = _municipios.value.find { it.id == id }?.nombre
            _homeConfirmationMessage.value = "✅ ${name ?: "Municipio"} set as home"
        }
    }

    fun clearSnackbarMessage() {
        _snackbarMessage.value = null
    }

    fun clearHomeConfirmationMessage() {
        _homeConfirmationMessage.value = null
    }

    fun syncMunicipiosToFirestore() {
        viewModelScope.launch {
            try {
                syncService.uploadMunicipios(_municipios.value)
                _syncSuccess.value = true
            } catch (e: Exception) {
                _syncSuccess.value = false
            }
        }
    }

    fun reloadFromFirestore() {
        viewModelScope.launch {
            try {
                val remoteMunicipios = syncService.downloadRemoteMunicipios()
                val current = _municipios.value
                val merged = (current + remoteMunicipios).distinctBy { it.id }

                _municipios.value = merged
                remoteMunicipios.forEach { municipio ->
                    fetchSnapshot(municipio.id)
                }
            } catch (e: Exception) {
                _syncSuccess.value = false
            }
        }
    }

    fun fetchHourlyForecast(municipioId: String) {
        viewModelScope.launch {
            try {
                val rawDtos = getHourlyForecastUseCase(municipioId)
                val items = rawDtos.flatMap { it.toHourlyForecastFullItems() }
                _hourlyFullForecasts.update { it + (municipioId to items) }
            } catch (e: Exception) {
                // Optional logging
            }
        }
    }

    private val _dailyForecasts = MutableStateFlow<Map<String, List<DailyForecast>>>(emptyMap())
    val dailyForecasts: StateFlow<Map<String, List<DailyForecast>>> = _dailyForecasts

    fun fetchDailyForecast(municipioId: String) {
        viewModelScope.launch {
            try {
                val result = getDailyForecastUseCase(municipioId)
                _dailyForecasts.update { it + (municipioId to result) }
            } catch (_: Exception) {}
        }
    }


    private suspend fun <T> retryIO(
        times: Int = 3,
        delayMillis: Long = 1000,
        block: suspend () -> T
    ): T {
        repeat(times - 1) {
            try {
                return block()
            } catch (_: Exception) {
                delay(delayMillis)
            }
        }
        return block()
    }

    fun importSnapshotFromUri(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val reader = InputStreamReader(inputStream)
                val gson = Gson()
                // Parse as array
                val snapshots: List<SnapshotReport> = gson.fromJson(
                    reader,
                    Array<SnapshotReport>::class.java
                )?.toList() ?: emptyList()
                inputStream?.close()

                var imported = 0
                var skipped = 0

                for (snapshot in snapshots) {
                    // Deduplication: by reportId (already in Firestore/local)
                    val exists = getSnapshotByReportIdUseCase(snapshot.reportId) != null
                    if (!exists) {
                        saveSnapshotReportUseCase(snapshot)
                        imported++
                    } else {
                        skipped++
                    }
                }
                // Optionally refresh UI state, depending on your flow
                loadMunicipiosAndSnapshots()

                _snackbarMessage.value = "Importados: $imported, Omitidos: $skipped"
            } catch (e: Exception) {
                _snackbarMessage.value = "Error al importar: ${e.message}"
            }
        }
    }

}
