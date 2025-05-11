package com.alexser.weathernote.domain.usecase

import com.alexser.weathernote.domain.model.SnapshotReport
import com.alexser.weathernote.domain.repository.SnapshotReportRepository
import javax.inject.Inject

class GenerateSnapshotReport @Inject constructor(
    private val repository: SnapshotReportRepository
) {
    suspend operator fun invoke(snapshot: SnapshotReport) {
        repository.saveSnapshotReport(snapshot)
    }
}
