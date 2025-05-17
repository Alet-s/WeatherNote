package com.alexser.weathernote.presentation.screens.municipios

import com.alexser.weathernote.domain.model.Snapshot

sealed class SnapshotUiState {
    object Loading : SnapshotUiState()
    data class Success(val snapshot: Snapshot) : SnapshotUiState()
    data class Error(val message: String) : SnapshotUiState()
}
