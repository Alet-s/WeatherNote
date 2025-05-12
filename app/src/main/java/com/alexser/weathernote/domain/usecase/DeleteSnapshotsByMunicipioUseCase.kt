package com.alexser.weathernote.domain.usecase

import com.alexser.weathernote.domain.repository.SnapshotReportRepository
import javax.inject.Inject

class DeleteSnapshotsByMunicipioUseCase @Inject constructor(
    private val repository: SnapshotReportRepository
) {
    suspend operator fun invoke(municipioId: String) {
        repository.deleteSnapshotsByMunicipioId(municipioId)
    }
}
