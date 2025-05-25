package com.alexser.weathernote.presentation.screens.home

import com.alexser.weathernote.data.remote.model.HourlyForecastFullItem
import com.alexser.weathernote.data.remote.model.HourlyForecastItem
import com.alexser.weathernote.domain.model.BasicWeatherForecast

sealed class SnapshotHomeUiState {
    object Idle : SnapshotHomeUiState()
    object Loading : SnapshotHomeUiState()
    data class Success(
        val data: BasicWeatherForecast,
        val hourly: List<HourlyForecastItem> = emptyList(),
        val hourlyFull: List<HourlyForecastFullItem>? = null
    ) : SnapshotHomeUiState()
    data class Error(val message: String) : SnapshotHomeUiState()
}
