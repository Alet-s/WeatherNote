package com.alexser.weathernote.presentation.screens.snapshot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexser.weathernote.domain.model.SnapshotFrequency
import com.alexser.weathernote.domain.model.SnapshotReport
import com.alexser.weathernote.domain.usecase.GetSnapshotReportsByMunicipioUseCase
import com.alexser.weathernote.domain.usecase.GetSnapshotFrequencyUseCase
import com.alexser.weathernote.domain.usecase.SaveSnapshotFrequencyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SnapshotMunicipioViewModel @Inject constructor(
    private val getSnapshotReportsByMunicipio: GetSnapshotReportsByMunicipioUseCase,
    private val getSnapshotFrequency: GetSnapshotFrequencyUseCase,
    private val saveSnapshotFrequency: SaveSnapshotFrequencyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SnapshotMunicipioUiState())
    val uiState: StateFlow<SnapshotMunicipioUiState> = _uiState

    fun loadSnapshotData(municipioId: String) {
        viewModelScope.launch {
            val snapshots = getSnapshotReportsByMunicipio(municipioId)
            val frequency = getSnapshotFrequency(municipioId)
            _uiState.value = _uiState.value.copy(
                municipioId = municipioId,
                municipioName = snapshots.firstOrNull()?.municipioName,
                snapshots = snapshots,
                selectedFrequency = frequency
            )
        }
    }

    fun updateFrequency(newFrequency: SnapshotFrequency) {
        val municipioId = _uiState.value.municipioId ?: return
        viewModelScope.launch {
            saveSnapshotFrequency(municipioId, newFrequency)
            _uiState.value = _uiState.value.copy(selectedFrequency = newFrequency)
        }
    }
}
