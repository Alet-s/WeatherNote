package com.alexser.weathernote.domain.repository

import com.alexser.weathernote.domain.model.SnapshotReport

interface SnapshotReportRepository {
    suspend fun saveSnapshotReport(snapshot: SnapshotReport)
    suspend fun getAllSnapshotsReports(): List<SnapshotReport>
    suspend fun deleteSnapshotsByMunicipioId(municipioId: String)
    suspend fun deleteSnapshot(snapshot: SnapshotReport)//Borra un municipio individual
}
