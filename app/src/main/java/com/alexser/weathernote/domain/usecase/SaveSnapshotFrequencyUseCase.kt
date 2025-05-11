package com.alexser.weathernote.domain.usecase

import com.alexser.weathernote.domain.model.SnapshotFrequency
import com.alexser.weathernote.domain.repository.SnapshotConfigRepository
import javax.inject.Inject

class SaveSnapshotFrequencyUseCase @Inject constructor(
    private val repository: SnapshotConfigRepository
) {
    suspend operator fun invoke(municipioId: String, frequency: SnapshotFrequency) {
        repository.saveSnapshotFrequency(municipioId, frequency)
    }
}
