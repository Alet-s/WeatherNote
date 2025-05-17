package com.alexser.weathernote.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexser.weathernote.data.firebase.AuthDataSource
import com.alexser.weathernote.data.local.HomeMunicipioPreferences
import com.alexser.weathernote.data.remote.mapper.toHourlyForecastFullItems
import com.alexser.weathernote.domain.model.SavedMunicipio
import com.alexser.weathernote.domain.usecase.AddMunicipioUseCase
import com.alexser.weathernote.domain.usecase.FindMunicipioByNameUseCase
import com.alexser.weathernote.domain.usecase.GetHourlyForecastUseCase
import com.alexser.weathernote.domain.usecase.GetBasicWeatherForecastUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.alexser.weathernote.data.remote.mapper.toHourlyForecastItems
import com.alexser.weathernote.data.remote.mapper.toSnapshotReport
import com.alexser.weathernote.domain.usecase.GenerateSnapshotReport
import java.time.LocalTime
import com.alexser.weathernote.domain.usecase.SaveSnapshotReportUseCase

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val getBasicWeatherForecastUseCase: GetBasicWeatherForecastUseCase,
    private val addMunicipioUseCase: AddMunicipioUseCase,
    private val findMunicipioByNameUseCase: FindMunicipioByNameUseCase,
    private val getHourlyForecastUseCase: GetHourlyForecastUseCase,
    private val generateSnapshotUseCase: GenerateSnapshotReport,
    private val saveSnapshotReportUseCase: SaveSnapshotReportUseCase,
    private val homePrefs: HomeMunicipioPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow<SnapshotUiState>(SnapshotUiState.Idle)
    val uiState: StateFlow<SnapshotUiState> = _uiState

    private val _searchInput = MutableStateFlow("")
    val searchInput: StateFlow<String> = _searchInput

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    init {
        // Observe changes in home municipio preference
        viewModelScope.launch {
            homePrefs.homeMunicipioId.collect { id ->
                if (!id.isNullOrBlank()) {
                    fetchSnapshot(id)
                } else {
                    _uiState.value = SnapshotUiState.Idle
                }
            }
        }
    }

    fun onSearchInputChanged(newInput: String) {
        _searchInput.value = newInput
    }

    fun searchAndFetchSnapshot() {
        viewModelScope.launch {
            _uiState.value = SnapshotUiState.Loading
            val id = findMunicipioByNameUseCase(_searchInput.value)
            if (id != null) {
                fetchSnapshot(id)
            } else {
                _uiState.value = SnapshotUiState.Error("Municipio not found")
            }
        }
    }

    fun fetchSnapshot(municipioId: String) {
        viewModelScope.launch {
            _uiState.value = SnapshotUiState.Loading
            val snapshotResult = getBasicWeatherForecastUseCase(municipioId)

            if (snapshotResult.isSuccess) {
                val snapshot = snapshotResult.getOrThrow()
                val hourlyDto = getHourlyForecastUseCase(municipioId)

                val hourlyItems = hourlyDto.firstOrNull()?.toHourlyForecastItems() ?: emptyList()
                val hourlyFullItems = hourlyDto.firstOrNull()?.toHourlyForecastFullItems() ?: emptyList()

                _uiState.value = SnapshotUiState.Success(
                    data = snapshot,
                    hourly = hourlyItems,
                    hourlyFull = hourlyFullItems
                )
            } else {
                _uiState.value = SnapshotUiState.Error(
                    snapshotResult.exceptionOrNull()?.localizedMessage ?: "Failed to load weather data"
                )
            }
        }
    }

    fun generateSnapshotManually() {
        val state = _uiState.value as? SnapshotUiState.Success ?: return
        val municipioId = state.data.cityId
        val municipioName = state.data.city
        val date = state.data.date
        val fullList = state.hourlyFull ?: return

        val currentHour = LocalTime.now().hour.toString().padStart(2, '0')
        val currentItem = fullList.find { it.hour == currentHour } ?: return

        val report = currentItem.toSnapshotReport(
            municipioId = municipioId,
            municipioName = municipioName,
            date = date
        )

        viewModelScope.launch {
            try {
                saveSnapshotReportUseCase(report)
                _snackbarMessage.emit("Snapshot saved successfully.")
            } catch (e: Exception) {
                _snackbarMessage.emit("Error saving snapshot.")
            }
        }
    }


    fun addToFavorites() {
        val snapshot = (_uiState.value as? SnapshotUiState.Success)?.data ?: return
        val municipio = SavedMunicipio(id = snapshot.cityId, nombre = snapshot.city)
        viewModelScope.launch {
            addMunicipioUseCase(municipio)
            homePrefs.setHomeMunicipioId(municipio.id)
        }
    }

    fun clearHomeMunicipio() {
        viewModelScope.launch {
            homePrefs.clearHomeMunicipioId()
            _uiState.value = SnapshotUiState.Idle
        }
    }


    fun logout() {
        authDataSource.signOut()
    }
}
