package com.alexser.weathernote.presentation.screens.snapshot

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexser.weathernote.data.remote.mapper.toSnapshotReport
import com.alexser.weathernote.domain.model.SavedMunicipio
import com.alexser.weathernote.domain.model.SnapshotReport
import com.alexser.weathernote.domain.usecase.*
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import toHourlyForecastFullItems
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

    fun loadSnapshotData(municipioId: String, municipioName: String) {
        viewModelScope.launch {
            val snapshots = getSnapshotReportsByMunicipio(municipioId)
            _uiState.value = _uiState.value.copy(
                municipioId = municipioId,
                municipioName = municipioName,
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
                Log.e("SNAPSHOT_GENERATE", "❌ Error generating snapshot: ${e.message}")
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
                Log.e("SNAPSHOT_DELETE", "❌ Error deleting snapshot: ${e.message}")
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
                Log.e("SNAPSHOT_DELETE_BATCH", "❌ Error deleting batch: ${e.message}")
            }
        }
    }

    fun saveSnapshotJsonToUri(context: Context, uri: Uri, reportIds: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val gson = Gson()
                val snapshots = reportIds.mapNotNull { getSnapshotByReportIdUseCase(it) }

                if (snapshots.isNotEmpty()) {
                    val json = gson.toJson(snapshots)
                    context.contentResolver.openOutputStream(uri)?.use { output ->
                        output.write(json.toByteArray())
                    }
                    Log.d("SNAPSHOT_IO", "✅ Saved JSON to selected URI")
                } else {
                    Log.w("SNAPSHOT_IO", "⚠️ No snapshots to save")
                }
            } catch (e: Exception) {
                Log.e("SNAPSHOT_IO", "❌ Error saving snapshot to URI: ${e.message}")
            }
        }
    }

    fun updateNoteForSnapshot(reportId: String, newNote: String) {
        viewModelScope.launch {
            try {
                val snapshot = getSnapshotByReportIdUseCase(reportId) ?: return@launch
                val updatedSnapshot = snapshot.copy(userNote = newNote)
                saveSnapshotReportUseCase(updatedSnapshot)

                val updatedList = getSnapshotReportsByMunicipio(snapshot.municipioId)
                _uiState.value = _uiState.value.copy(snapshots = updatedList)
            } catch (e: Exception) {
                Log.e("NOTE_UPDATE", "❌ Failed to update note: ${e.message}")
            }
        }
    }

}
