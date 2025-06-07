package com.alexser.weathernote.domain.usecase

import com.alexser.weathernote.domain.model.SnapshotReport
import com.alexser.weathernote.domain.repository.SnapshotReportRepository
import javax.inject.Inject

/**
 * Use case para eliminar múltiples informes meteorológicos (snapshots) en lote.
 *
 * @property repository Repositorio que gestiona las operaciones de SnapshotReport.
 */
class DeleteBatchSnapshotsUseCase @Inject constructor(
    private val repository: SnapshotReportRepository
) {
    /**
     * Ejecuta la eliminación de una lista de informes meteorológicos.
     *
     * @param snapshots Lista de [SnapshotReport] que serán eliminados.
     */
    suspend operator fun invoke(snapshots: List<SnapshotReport>) {
        repository.deleteBatchSnapshots(snapshots)
    }
}
