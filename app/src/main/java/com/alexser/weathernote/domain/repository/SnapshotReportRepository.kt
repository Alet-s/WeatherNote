package com.alexser.weathernote.domain.repository

import com.alexser.weathernote.domain.model.SnapshotReport

/**
 * Repositorio para gestionar operaciones CRUD sobre informes meteorológicos (SnapshotReport).
 */
interface SnapshotReportRepository {

    /**
     * Guarda un informe meteorológico.
     *
     * @param snapshot Informe meteorológico a guardar.
     */
    suspend fun saveSnapshotReport(snapshot: SnapshotReport)

    /**
     * Obtiene todos los informes meteorológicos almacenados.
     *
     * @return Lista de todos los [SnapshotReport] guardados.
     */
    suspend fun getAllSnapshotsReports(): List<SnapshotReport>

    /**
     * Elimina todos los informes asociados a un municipio dado.
     *
     * @param municipioId Identificador del municipio cuyos informes se eliminarán.
     */
    suspend fun deleteSnapshotsByMunicipioId(municipioId: String)

    /**
     * Elimina un informe meteorológico individual.
     *
     * @param snapshot Informe a eliminar.
     */
    suspend fun deleteSnapshot(snapshot: SnapshotReport)

    /**
     * Elimina varios informes meteorológicos en lote.
     *
     * @param snapshots Lista de informes a eliminar.
     */
    suspend fun deleteBatchSnapshots(snapshots: List<SnapshotReport>)

    /**
     * Obtiene un informe meteorológico dado su identificador único.
     *
     * @param reportId Identificador único del informe.
     * @return El informe correspondiente o null si no existe.
     */
    suspend fun getSnapshotByReportId(reportId: String): SnapshotReport?
}
