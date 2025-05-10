package com.alexser.weathernote.presentation.screens.municipios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexser.weathernote.data.firebase.MunicipioSyncService
import com.alexser.weathernote.data.remote.mapper.toHourlyForecastFullItems
import com.alexser.weathernote.data.remote.model.HourlyForecastFullItem
import com.alexser.weathernote.data.remote.model.HourlyForecastItem
import com.alexser.weathernote.domain.model.SavedMunicipio
import com.alexser.weathernote.domain.model.Snapshot
import com.alexser.weathernote.domain.usecase.AddMunicipioUseCase
import com.alexser.weathernote.domain.usecase.FindMunicipioByNameUseCase
import com.alexser.weathernote.domain.usecase.GetHourlyForecastUseCase
import com.alexser.weathernote.domain.usecase.GetSavedMunicipiosUseCase
import com.alexser.weathernote.domain.usecase.GetSnapshotUseCase
import com.alexser.weathernote.domain.usecase.RemoveMunicipioUseCase
//import com.alexser.weathernote.domain.usecase.SuggestMunicipiosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class MunicipiosScreenViewModel @Inject constructor(
    private val getSavedMunicipiosUseCase: GetSavedMunicipiosUseCase,
    private val addMunicipioUseCase: AddMunicipioUseCase,
    private val removeMunicipioUseCase: RemoveMunicipioUseCase,
    private val getSnapshotUseCase: GetSnapshotUseCase,
    private val findMunicipioByNameUseCase: FindMunicipioByNameUseCase,
    private val syncService: MunicipioSyncService,
    private val getHourlyForecastUseCase: GetHourlyForecastUseCase
    //TODO: implementar sugerencias en vivo cuando escribas
    //private val suggestMunicipiosUseCase: SuggestMunicipiosUseCase,
) : ViewModel() {

    private val _municipios = MutableStateFlow<List<SavedMunicipio>>(emptyList())
    val municipios: StateFlow<List<SavedMunicipio>> = _municipios

    private val _snapshots = MutableStateFlow<Map<String, Snapshot?>>(emptyMap())
    val snapshots: StateFlow<Map<String, Snapshot?>> = _snapshots

    private val _syncSuccess = MutableStateFlow<Boolean?>(null)
    val syncSuccess: StateFlow<Boolean?> = _syncSuccess

    private val _suggestions = MutableStateFlow<List<String>>(emptyList())
    val suggestions: StateFlow<List<String>> get() = _suggestions

    private val _hourlyFullForecasts = MutableStateFlow<Map<String, List<HourlyForecastFullItem>>>(emptyMap())
    val hourlyFullForecasts: StateFlow<Map<String, List<HourlyForecastFullItem>>> = _hourlyFullForecasts


    init {
        viewModelScope.launch {
            try {
                val remoteMunicipios = syncService.downloadRemoteMunicipios()
                remoteMunicipios.forEach { municipio ->
                    addMunicipioUseCase(municipio)
                }
            } catch (e: Exception) {
                _syncSuccess.value = false
            }

            // ✅ Only collect after Firestore sync completes
            getSavedMunicipiosUseCase().collect { savedList ->
                _municipios.value = savedList
                savedList.forEach { municipio ->
                    if (!_snapshots.value.containsKey(municipio.id)) {
                        val snapshot = getSnapshotUseCase(municipio.id).getOrNull()
                        _snapshots.update { it + (municipio.id to snapshot) }
                    }
                }
            }
        }
    }




    fun addMunicipioByName(name: String) {
        viewModelScope.launch {
            val id = findMunicipioByNameUseCase(name)
            if (id != null) {
                val alreadyExists = _municipios.value.any { it.id == id }
                if (!alreadyExists) {
                    val newMunicipio = SavedMunicipio(id = id, nombre = name)
                    addMunicipioUseCase(newMunicipio)

                    val snapshot = getSnapshotUseCase(id).getOrNull()
                    _snapshots.update { it + (id to snapshot) }

                    syncMunicipiosToFirestore()
                }
            }
        }
    }

    fun removeMunicipio(id: String) {
        viewModelScope.launch {
            val toRemove = _municipios.value.find { it.id == id } ?: return@launch
            removeMunicipioUseCase(toRemove)
            _municipios.update { it.filterNot { it.id == id } }
            _snapshots.update { it - id }

            syncMunicipiosToFirestore()
        }
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

//    fun onNameInputChanged(newInput: String) {
//        viewModelScope.launch {
//            _suggestions.value = suggestMunicipiosUseCase(newInput)
//        }
//    }

    fun reloadFromFirestore() {
        viewModelScope.launch {
            try {
                val remoteMunicipios = syncService.downloadRemoteMunicipios()
                _municipios.value = remoteMunicipios

                remoteMunicipios.forEach { municipio ->
                    val snapshot = getSnapshotUseCase(municipio.id).getOrNull()
                    _snapshots.update { it + (municipio.id to snapshot) }
                }
            } catch (e: Exception) {
                _syncSuccess.value = false
            }
        }
    }

    fun loadFullForecastForMunicipio(id: String) {
        viewModelScope.launch {
            try {
                val rawDtos = getHourlyForecastUseCase(id)
                val fullItems = rawDtos.flatMap { it.toHourlyForecastFullItems() }
                _hourlyFullForecasts.update { it + (id to fullItems) }
            } catch (e: Exception) {
                // You can log the error if needed
            }
        }
    }

    private val _fullForecasts = MutableStateFlow<Map<String, List<HourlyForecastFullItem>>>(emptyMap())
    val fullForecasts: StateFlow<Map<String, List<HourlyForecastFullItem>>> = _fullForecasts

    fun fetchHourlyForecast(municipioId: String) {
        viewModelScope.launch {
            try {
                val rawDtos = getHourlyForecastUseCase(municipioId)
                val items = rawDtos.flatMap { it.toHourlyForecastFullItems() }
                _fullForecasts.update { it + (municipioId to items) }
            } catch (e: Exception) {
                // handle error if needed
            }
        }
    }



    fun resetSyncStatus() {
        _syncSuccess.value = null
    }

}
