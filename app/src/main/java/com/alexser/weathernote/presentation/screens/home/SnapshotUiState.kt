package com.alexser.weathernote.presentation.screens.home

import com.alexser.weathernote.data.remote.model.HourlyForecastFullItem
import com.alexser.weathernote.data.remote.model.HourlyForecastItem
import com.alexser.weathernote.domain.model.Snapshot

sealed class SnapshotUiState {
    object Idle : SnapshotUiState()
    object Loading : SnapshotUiState()
    data class Success(
        val data: Snapshot,
        val hourly: List<HourlyForecastItem> = emptyList(),
        val hourlyFull: List<HourlyForecastFullItem>? = null
    ) : SnapshotUiState()
    data class Error(val message: String) : SnapshotUiState()
}
