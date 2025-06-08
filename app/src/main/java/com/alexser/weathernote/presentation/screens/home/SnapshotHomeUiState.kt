package com.alexser.weathernote.presentation.screens.home

import com.alexser.weathernote.data.remote.model.HourlyForecastFullItem
import com.alexser.weathernote.data.remote.model.HourlyForecastItem
import com.alexser.weathernote.domain.model.BasicWeatherForecast

/**
 * Representa el estado de la interfaz de usuario en la pantalla principal (HomeScreen).
 * Este estado se expone como un [StateFlow] en el ViewModel para reaccionar a cambios.
 */
sealed class SnapshotHomeUiState {

    /**
     * Estado inactivo. Se utiliza cuando no hay municipio principal seleccionado.
     */
    object Idle : SnapshotHomeUiState()

    /**
     * Estado de carga. Indica que se está obteniendo la información meteorológica.
     */
    object Loading : SnapshotHomeUiState()

    /**
     * Estado de éxito. Contiene los datos meteorológicos obtenidos correctamente.
     *
     * @property data Predicción meteorológica básica (fecha, municipio, temperatura, etc.).
     * @property hourly Lista de predicciones horarias simplificadas (por hora).
     * @property hourlyFull Lista detallada de predicciones horarias con todos los parámetros meteorológicos.
     */
    data class Success(
        val data: BasicWeatherForecast,
        val hourly: List<HourlyForecastItem> = emptyList(),
        val hourlyFull: List<HourlyForecastFullItem>? = null
    ) : SnapshotHomeUiState()

    /**
     * Estado de error. Indica que ocurrió un fallo al obtener los datos meteorológicos.
     *
     * @property message Mensaje descriptivo del error.
     */
    data class Error(val message: String) : SnapshotHomeUiState()
}
