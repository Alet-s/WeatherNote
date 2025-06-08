package com.alexser.weathernote.presentation.screens.municipios

import com.alexser.weathernote.domain.model.BasicWeatherForecast

/**
 * Representa los distintos estados de UI para la pantalla de snapshot de un municipio.
 */
sealed class SnapshotMunicipioUiState {

    /**
     * Estado en el que la pantalla está cargando datos.
     */
    object Loading : SnapshotMunicipioUiState()

    /**
     * Estado que indica que la carga fue exitosa y contiene la información meteorológica básica.
     *
     * @param basicWeatherForecast Información meteorológica básica obtenida para el municipio.
     */
    data class Success(val basicWeatherForecast: BasicWeatherForecast) : SnapshotMunicipioUiState()

    /**
     * Estado que indica que ocurrió un error durante la carga.
     *
     * @param message Mensaje descriptivo del error ocurrido.
     */
    data class Error(val message: String) : SnapshotMunicipioUiState()
}
