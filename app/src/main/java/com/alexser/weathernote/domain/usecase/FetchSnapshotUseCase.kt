package com.alexser.weathernote.domain.usecase

import com.alexser.weathernote.domain.model.Snapshot
import com.alexser.weathernote.domain.repository.WeatherRepository
import javax.inject.Inject

class FetchSnapshotUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(municipioId: String): Snapshot {
        return repository.getSnapshot(municipioId)
    }
}
