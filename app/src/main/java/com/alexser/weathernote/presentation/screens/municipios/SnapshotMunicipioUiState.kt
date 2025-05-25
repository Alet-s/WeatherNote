package com.alexser.weathernote.presentation.screens.municipios

import com.alexser.weathernote.domain.model.BasicWeatherForecast

sealed class SnapshotMunicipioUiState {
    object Loading : SnapshotMunicipioUiState()
    data class Success(val basicWeatherForecast: BasicWeatherForecast) : SnapshotMunicipioUiState()
    data class Error(val message: String) : SnapshotMunicipioUiState()
}
