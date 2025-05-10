package com.alexser.weathernote.presentation.screens.home

import com.alexser.weathernote.domain.model.Snapshot

sealed class SnapshotUiState {
    object Idle : SnapshotUiState() // ← Add this
    object Loading : SnapshotUiState()
    data class Success(val data: Snapshot) : SnapshotUiState()
    data class Error(val message: String) : SnapshotUiState()
}
