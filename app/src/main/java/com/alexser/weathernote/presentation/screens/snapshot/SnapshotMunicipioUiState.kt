package com.alexser.weathernote.presentation.screens.snapshot

import com.alexser.weathernote.domain.model.SnapshotFrequency
import com.alexser.weathernote.domain.model.SnapshotReport

data class SnapshotMunicipioUiState(
    val municipioId: String? = null,
    val municipioName: String? = null,
    val snapshots: List<SnapshotReport> = emptyList()
)
