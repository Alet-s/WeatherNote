package com.alexser.weathernote.presentation.screens.home

import com.alexser.weathernote.data.remote.model.HourlyForecastItem
import com.alexser.weathernote.domain.model.Snapshot

sealed class SnapshotUiState {
    object Idle : SnapshotUiState()
    object Loading : SnapshotUiState()
    data class Success(
        val data: Snapshot,
        val hourly: List<HourlyForecastItem> = emptyList()
    ) : SnapshotUiState()
    data class Error(val message: String) : SnapshotUiState()
}
