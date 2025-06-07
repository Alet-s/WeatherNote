package com.alexser.weathernote.domain.usecase

import com.alexser.weathernote.domain.model.SnapshotReport
import com.alexser.weathernote.domain.repository.SnapshotReportRepository
import javax.inject.Inject

/**
 * Use case para eliminar un informe meteorológico (snapshot) individual.
 *
 * @property repository Repositorio que gestiona las operaciones de SnapshotReport.
 */
class DeleteSnapshotReportUseCase @Inject constructor(
    private val repository: SnapshotReportRepository
) {

    /**
     * Ejecuta la eliminación de un informe meteorológico específico.
     *
     * @param snapshot El [SnapshotReport] que será eliminado.
     */
    suspend operator fun invoke(snapshot: SnapshotReport) {
        repository.deleteSnapshot(snapshot)
    }
}
