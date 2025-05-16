package com.alexser.weathernote.presentation.screens.snapshot

import android.content.Context
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexser.weathernote.data.remote.mapper.toHourlyForecastFullItems
import com.alexser.weathernote.data.remote.mapper.toSnapshotReport
import com.alexser.weathernote.domain.model.SavedMunicipio
import com.alexser.weathernote.domain.model.SnapshotReport
import com.alexser.weathernote.domain.usecase.DeleteBatchSnapshotsUseCase
import com.alexser.weathernote.domain.usecase.DeleteSnapshotReportUseCase
import com.alexser.weathernote.domain.usecase.GetHourlyForecastUseCase
import com.alexser.weathernote.domain.usecase.GetSnapshotByReportIdUseCase
import com.alexser.weathernote.domain.usecase.GetSnapshotReportsByMunicipioUseCase
import com.alexser.weathernote.domain.usecase.SaveSnapshotReportUseCase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SnapshotMunicipioViewModel @Inject constructor(
    private val getSnapshotReportsByMunicipio: GetSnapshotReportsByMunicipioUseCase,
    private val getHourlyForecastUseCase: GetHourlyForecastUseCase,
    private val saveSnapshotReportUseCase: SaveSnapshotReportUseCase,
    private val deleteSnapshotReportUseCase: DeleteSnapshotReportUseCase,
    private val deleteBatchSnapshotsUseCase: DeleteBatchSnapshotsUseCase,
    private val getSnapshotByReportIdUseCase: GetSnapshotByReportIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SnapshotMunicipioUiState())
    val uiState: StateFlow<SnapshotMunicipioUiState> = _uiState

    fun loadSnapshotData(municipioId: String) {
        viewModelScope.launch {
            val snapshots = getSnapshotReportsByMunicipio(municipioId)
            _uiState.value = _uiState.value.copy(
                municipioId = municipioId,
                municipioName = snapshots.firstOrNull()?.municipioName,
                snapshots = snapshots
            )
        }
    }

    fun generateSnapshotManually(municipioId: SavedMunicipio) {
        val id = _uiState.value.municipioId ?: return
        val name = _uiState.value.municipioName ?: "Desconocido"

        viewModelScope.launch {
            try {
                val rawDtos = getHourlyForecastUseCase(id)
                val fullItems = rawDtos.flatMap { it.toHourlyForecastFullItems() }
                val currentHour = LocalDateTime.now().hour.toString().padStart(2, '0')

                val matching = fullItems.find { it.hour.padStart(2, '0').startsWith(currentHour) }
                if (matching == null) return@launch

                val snapshot = matching.toSnapshotReport(
                    municipioId = id,
                    municipioName = name,
                    date = LocalDate.now().toString()
                ).copy(reportId = java.util.UUID.randomUUID().toString())

                saveSnapshotReportUseCase(snapshot)

                val updated = getSnapshotReportsByMunicipio(id)
                _uiState.value = _uiState.value.copy(snapshots = updated)
            } catch (e: Exception) {
                println("❌ Error generating snapshot: ${e.message}")
            }
        }
    }

    fun deleteSnapshot(snapshot: SnapshotReport) {
        viewModelScope.launch {
            try {
                deleteSnapshotReportUseCase(snapshot)
                val updated = getSnapshotReportsByMunicipio(snapshot.municipioId)
                _uiState.value = _uiState.value.copy(snapshots = updated)
            } catch (e: Exception) {
                println("❌ Error deleting snapshot: ${e.message}")
            }
        }
    }

    fun deleteSnapshotsInBatch(snapshots: List<SnapshotReport>) {
        viewModelScope.launch {
            try {
                deleteBatchSnapshotsUseCase(snapshots)
                val updated = getSnapshotReportsByMunicipio(_uiState.value.municipioId ?: return@launch)
                _uiState.value = _uiState.value.copy(snapshots = updated)
            } catch (e: Exception) {
                println("❌ Error deleting batch: ${e.message}")
            }
        }
    }

    fun downloadSnapshotsAsJsonById(context: Context, reportIds: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val gson = Gson()
                val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!dir.exists()) dir.mkdirs()

                for (reportId in reportIds) {
                    val snapshot = getSnapshotByReportIdUseCase(reportId)
                    if (snapshot != null) {
                        val json = gson.toJson(snapshot)
                        val fileName = "Snapshot_${snapshot.municipioName}_${snapshot.timestamp}_${snapshot.reportId.take(8)}.json"
                        val file = File(dir, fileName.replace(":", "-"))
                        file.writeText(json)
                    }
                }
            } catch (e: Exception) {
                println("❌ Error writing snapshot to file: ${e.message}")
            }
        }
    }


}
