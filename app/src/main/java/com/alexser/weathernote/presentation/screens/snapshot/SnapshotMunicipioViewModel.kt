package com.alexser.weathernote.presentation.screens.snapshot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexser.weathernote.data.remote.mapper.toHourlyForecastFullItems
import com.alexser.weathernote.data.remote.mapper.toSnapshotReport
import com.alexser.weathernote.domain.model.SnapshotFrequency
import com.alexser.weathernote.domain.model.SnapshotReport
import com.alexser.weathernote.domain.usecase.GetHourlyForecastUseCase
import com.alexser.weathernote.domain.usecase.GetSnapshotReportsByMunicipioUseCase
import com.alexser.weathernote.domain.usecase.GetSnapshotFrequencyUseCase
import com.alexser.weathernote.domain.usecase.SaveSnapshotFrequencyUseCase
import com.alexser.weathernote.domain.usecase.SaveSnapshotReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SnapshotMunicipioViewModel @Inject constructor(
    private val getSnapshotReportsByMunicipio: GetSnapshotReportsByMunicipioUseCase,
    private val getSnapshotFrequency: GetSnapshotFrequencyUseCase,
    private val saveSnapshotFrequency: SaveSnapshotFrequencyUseCase,
    private val getHourlyForecastUseCase: GetHourlyForecastUseCase,
    private val saveSnapshotReportUseCase: SaveSnapshotReportUseCase
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

    fun generateSnapshotManually() {
        val municipioId = _uiState.value.municipioId ?: return
        val municipioName = _uiState.value.municipioName ?: "Desconocido"

        viewModelScope.launch {
            try {
                println("🔍 Fetching hourly forecast for $municipioId")

                val rawDtos = getHourlyForecastUseCase(municipioId)
                val fullItems = rawDtos.flatMap { it.toHourlyForecastFullItems() }
                val currentHour = LocalDateTime.now().hour.toString().padStart(2, '0')

                println("🕒 Looking for forecast at hour $currentHour")
                val matching = fullItems.find { it.hour.padStart(2, '0').startsWith(currentHour) }

                if (matching == null) {
                    println("⚠️ No matching forecast found for $currentHour")
                    return@launch
                }

                val snapshot = matching.toSnapshotReport(
                    municipioId = municipioId,
                    municipioName = municipioName,
                    date = LocalDate.now().toString()
                )

                println("💾 Saving snapshot: $snapshot")
                saveSnapshotReportUseCase(snapshot)
                println("✅ Snapshot saved!")

                val updated = getSnapshotReportsByMunicipio(municipioId)
                _uiState.value = _uiState.value.copy(snapshots = updated)

            } catch (e: Exception) {
                println("❌ Error generating snapshot: ${e.message}")
            }
        }
    }

    fun updateFrequency(newFrequency: SnapshotFrequency) {
        val municipioId = _uiState.value.municipioId ?: return
        viewModelScope.launch {
            saveSnapshotFrequency(municipioId, newFrequency)
            _uiState.value = _uiState.value.copy(selectedFrequency = newFrequency)
        }
    }

    fun saveFrequency() {
        val municipioId = _uiState.value.municipioId ?: return
        val frequency = _uiState.value.selectedFrequency
        viewModelScope.launch {
            saveSnapshotFrequency(municipioId, frequency)
        }
    }

}
