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

/**
 * ViewModel para la pantalla que gestiona los snapshots meteorológicos de un municipio.
 *
 * Se encarga de cargar, generar, eliminar, exportar y actualizar notas de los SnapshotReports.
 *
 * @property getSnapshotReportsByMunicipio Caso de uso para obtener snapshots de un municipio.
 * @property getHourlyForecastUseCase Caso de uso para obtener la predicción horaria.
 * @property saveSnapshotReportUseCase Caso de uso para guardar un snapshot.
 * @property deleteSnapshotReportUseCase Caso de uso para eliminar un snapshot individual.
 * @property deleteBatchSnapshotsUseCase Caso de uso para eliminar múltiples snapshots.
 * @property getSnapshotByReportIdUseCase Caso de uso para obtener un snapshot por su ID de reporte.
 */
@HiltViewModel
class SnapshotMunicipioViewModel @Inject constructor(
    private val getSnapshotReportsByMunicipio: GetSnapshotReportsByMunicipioUseCase,
    private val getHourlyForecastUseCase: GetHourlyForecastUseCase,
    private val saveSnapshotReportUseCase: SaveSnapshotReportUseCase,
    private val deleteSnapshotReportUseCase: DeleteSnapshotReportUseCase,
    private val deleteBatchSnapshotsUseCase: DeleteBatchSnapshotsUseCase,
    private val getSnapshotByReportIdUseCase: GetSnapshotByReportIdUseCase
) : ViewModel() {

    /** Estado UI observable que contiene datos actuales del municipio y sus snapshots */
    private val _uiState = MutableStateFlow(SnapshotMunicipioUiState())
    val uiState: StateFlow<SnapshotMunicipioUiState> = _uiState

    /**
     * Carga los snapshots asociados a un municipio dado.
     *
     * @param municipioId ID único del municipio.
     * @param municipioName Nombre del municipio.
     */
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

    /**
     * Genera manualmente un snapshot actual basado en la predicción horaria para el municipio actual.
     *
     * @param municipio El municipio guardado para el que se genera el snapshot.
     */
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

    /**
     * Elimina un snapshot específico y actualiza la lista de snapshots en UI.
     *
     * @param snapshot SnapshotReport a eliminar.
     */

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

    /**
     * Elimina múltiples snapshots en lote y actualiza la UI.
     *
     * @param snapshots Lista de SnapshotReport a eliminar.
     */
    fun deleteSnapshotsInBatch(snapshots: List<SnapshotReport>) {
        viewModelScope.launch {
            try {
                deleteBatchSnapshotsUseCase(snapshots)
                val updated =
                    getSnapshotReportsByMunicipio(_uiState.value.municipioId ?: return@launch)
                _uiState.value = _uiState.value.copy(snapshots = updated)
            } catch (e: Exception) {
                Log.e("SNAPSHOT_DELETE_BATCH", "❌ Error deleting batch: ${e.message}")
            }
        }
    }

    /**
     * Exporta snapshots seleccionados a un archivo JSON en la URI proporcionada.
     *
     * @param context Contexto para acceder al ContentResolver.
     * @param uri URI destino para guardar el archivo JSON.
     * @param reportIds Lista de IDs de los snapshots a exportar.
     */
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

    /**
     * Actualiza la nota de usuario asociada a un snapshot y refresca la UI.
     *
     * @param reportId ID del snapshot a actualizar.
     * @param newNote Nuevo texto de la nota a guardar.
     */
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
