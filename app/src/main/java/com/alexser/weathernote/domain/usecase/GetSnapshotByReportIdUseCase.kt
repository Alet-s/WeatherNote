package com.alexser.weathernote.domain.usecase

import com.alexser.weathernote.domain.model.SnapshotReport
import com.alexser.weathernote.domain.repository.SnapshotReportRepository
import javax.inject.Inject

class GetSnapshotByReportIdUseCase @Inject constructor(
    private val repository: SnapshotReportRepository
) {
    suspend operator fun invoke(reportId: String): SnapshotReport? {
        return repository.getSnapshotByReportId(reportId)
    }
}
