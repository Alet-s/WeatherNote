package com.alexser.weathernote.domain.usecase

import com.alexser.weathernote.domain.model.SnapshotReport
import com.alexser.weathernote.domain.repository.SnapshotReportRepository
import javax.inject.Inject

class GetSnapshotReportsByMunicipioUseCase @Inject constructor(
    private val repository: SnapshotReportRepository
) {
    suspend operator fun invoke(municipioId: String): List<SnapshotReport> {
        return repository.getAllSnapshotsReports()
            .filter { it.municipioId == municipioId }
    }
}
