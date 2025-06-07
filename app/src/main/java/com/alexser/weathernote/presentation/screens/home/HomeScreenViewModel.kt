package com.alexser.weathernote.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexser.weathernote.data.firebase.AuthDataSource
import com.alexser.weathernote.data.local.HomeMunicipioPreferences
import com.alexser.weathernote.domain.usecase.AddMunicipioUseCase
import com.alexser.weathernote.domain.usecase.FindMunicipioByNameUseCase
import com.alexser.weathernote.domain.usecase.GetHourlyForecastUseCase
import com.alexser.weathernote.domain.usecase.GetBasicWeatherForecastUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.alexser.weathernote.data.remote.mapper.toSnapshotReport
import com.alexser.weathernote.domain.model.DailyForecast
import com.alexser.weathernote.domain.usecase.GenerateSnapshotReport
import com.alexser.weathernote.domain.usecase.GetDailyForecastUseCase
import java.time.LocalTime
import com.alexser.weathernote.domain.usecase.SaveSnapshotReportUseCase
import toHourlyForecastFullItems
import toHourlyForecastItems

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val getBasicWeatherForecastUseCase: GetBasicWeatherForecastUseCase,
    private val addMunicipioUseCase: AddMunicipioUseCase,
    private val findMunicipioByNameUseCase: FindMunicipioByNameUseCase,
    private val getHourlyForecastUseCase: GetHourlyForecastUseCase,
    private val generateSnapshotUseCase: GenerateSnapshotReport,
    private val saveSnapshotReportUseCase: SaveSnapshotReportUseCase,
    private val homePrefs: HomeMunicipioPreferences,
    private val getDailyForecastUseCase: GetDailyForecastUseCase,
    ) : ViewModel() {

    private val _uiState = MutableStateFlow<SnapshotHomeUiState>(SnapshotHomeUiState.Idle)
    val uiState: StateFlow<SnapshotHomeUiState> = _uiState

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    private val _dailyForecasts = MutableStateFlow<List<DailyForecast>>(emptyList())
    val dailyForecasts: StateFlow<List<DailyForecast>> = _dailyForecasts


    init {
        // Observe changes in home municipio preference
        viewModelScope.launch {
            homePrefs.homeMunicipioId.collect { id ->
                if (!id.isNullOrBlank()) {
                    fetchBWeatherForecast(id)
                } else {
                    _uiState.value = SnapshotHomeUiState.Idle
                }
            }
        }
    }

    fun fetchBWeatherForecast(municipioId: String) {
        viewModelScope.launch {
            _uiState.value = SnapshotHomeUiState.Loading
            val bWeatherForecastResult = getBasicWeatherForecastUseCase(municipioId)

            if (bWeatherForecastResult.isSuccess) {
                val snapshot = bWeatherForecastResult.getOrThrow()
                val hourlyDto = getHourlyForecastUseCase(municipioId)

                val hourlyItems = hourlyDto.firstOrNull()?.toHourlyForecastItems() ?: emptyList()
                val hourlyFullItems = hourlyDto.firstOrNull()?.toHourlyForecastFullItems() ?: emptyList()

                val daily = getDailyForecastUseCase(municipioId)
                _dailyForecasts.value = daily

                _uiState.value = SnapshotHomeUiState.Success(
                    data = snapshot,
                    hourly = hourlyItems,
                    hourlyFull = hourlyFullItems
                )
            } else {
                _uiState.value = SnapshotHomeUiState.Error(
                    bWeatherForecastResult.exceptionOrNull()?.localizedMessage ?: "Failed to load weather data"
                )
            }
        }
    }

    fun generateSnapshotManually() {
        val state = _uiState.value as? SnapshotHomeUiState.Success ?: return
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


    fun clearHomeMunicipio() {
        viewModelScope.launch {
            homePrefs.clearHomeMunicipioId()
            _uiState.value = SnapshotHomeUiState.Idle
        }
    }


    fun logout() {
        authDataSource.signOut()
    }

    fun showSnackbar(message: String) {
        viewModelScope.launch {
            _snackbarMessage.emit(message)
        }
    }


}
