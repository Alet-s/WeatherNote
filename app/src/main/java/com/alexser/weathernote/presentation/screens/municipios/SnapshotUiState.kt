package com.alexser.weathernote.presentation.screens.municipios

import com.alexser.weathernote.domain.model.BasicWeatherForecast

sealed class SnapshotUiState {
    object Loading : SnapshotUiState()
    data class Success(val basicWeatherForecast: BasicWeatherForecast) : SnapshotUiState()
    data class Error(val message: String) : SnapshotUiState()
}
