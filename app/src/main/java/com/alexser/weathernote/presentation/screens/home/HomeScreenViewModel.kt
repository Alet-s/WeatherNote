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

/**
 * ViewModel responsable de gestionar el estado y lógica de la pantalla principal (HomeScreen).
 * Se encarga de obtener predicciones meteorológicas, generar snapshots y manejar preferencias del usuario.
 *
 * @property authDataSource Fuente de autenticación para cerrar sesión.
 * @property getBasicWeatherForecastUseCase Caso de uso para obtener la predicción meteorológica básica.
 * @property addMunicipioUseCase Caso de uso para añadir un municipio favorito.
 * @property findMunicipioByNameUseCase Caso de uso para buscar municipios por nombre.
 * @property getHourlyForecastUseCase Caso de uso para obtener predicciones horarias.
 * @property generateSnapshotUseCase Caso de uso para generar un SnapshotReport.
 * @property saveSnapshotReportUseCase Caso de uso para guardar un SnapshotReport en Firestore.
 * @property homePrefs Preferencias locales del municipio principal.
 * @property getDailyForecastUseCase Caso de uso para obtener la predicción diaria extendida.
 */
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

    /** Estado observable de la pantalla principal. */
    private val _uiState = MutableStateFlow<SnapshotHomeUiState>(SnapshotHomeUiState.Idle)
    val uiState: StateFlow<SnapshotHomeUiState> = _uiState

    /** Flujo para emitir mensajes a un snackbar. */
    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    /** Flujo de datos con la predicción diaria extendida. */
    private val _dailyForecasts = MutableStateFlow<List<DailyForecast>>(emptyList())
    val dailyForecasts: StateFlow<List<DailyForecast>> = _dailyForecasts


    init {
        // Al iniciar, observa el ID del municipio principal guardad
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

    /**
     * Obtiene la predicción básica, horaria y diaria para el municipio especificado.
     *
     * @param municipioId ID del municipio a consultar.
     */
    fun fetchBWeatherForecast(municipioId: String) {
        viewModelScope.launch {
            _uiState.value = SnapshotHomeUiState.Loading
            val bWeatherForecastResult = getBasicWeatherForecastUseCase(municipioId)

            if (bWeatherForecastResult.isSuccess) {
                val snapshot = bWeatherForecastResult.getOrThrow()
                val hourlyDto = getHourlyForecastUseCase(municipioId)

                val hourlyItems = hourlyDto.firstOrNull()?.toHourlyForecastItems() ?: emptyList()
                val hourlyFullItems =
                    hourlyDto.firstOrNull()?.toHourlyForecastFullItems() ?: emptyList()

                val daily = getDailyForecastUseCase(municipioId)
                _dailyForecasts.value = daily

                _uiState.value = SnapshotHomeUiState.Success(
                    data = snapshot,
                    hourly = hourlyItems,
                    hourlyFull = hourlyFullItems
                )
            } else {
                _uiState.value = SnapshotHomeUiState.Error(
                    bWeatherForecastResult.exceptionOrNull()?.localizedMessage
                        ?: "Failed to load weather data"
                )
            }
        }
    }

    /**
     * Genera manualmente un SnapshotReport a partir de los datos actuales y lo guarda en Firestore.
     */
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
                _snackbarMessage.emit("Snapshot guardado correctamente.")
            } catch (e: Exception) {
                _snackbarMessage.emit("Error Guardando Snapshot.")
            }
        }
    }

    /**
     * Borra el municipio configurado como principal en preferencias locales.
     */
    fun clearHomeMunicipio() {
        viewModelScope.launch {
            homePrefs.clearHomeMunicipioId()
            _uiState.value = SnapshotHomeUiState.Idle
        }
    }

    /**
     * Emite un mensaje personalizado al snackbar.
     *
     * @param message Texto a mostrar.
     */
    fun showSnackbar(message: String) {
        viewModelScope.launch {
            _snackbarMessage.emit(message)
        }
    }


}
