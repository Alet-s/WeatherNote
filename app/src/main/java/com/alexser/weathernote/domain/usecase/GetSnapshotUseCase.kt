package com.alexser.weathernote.domain.usecase

import com.alexser.weathernote.domain.model.Snapshot
import com.alexser.weathernote.domain.repository.WeatherRepository
import javax.inject.Inject

class GetSnapshotUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(municipioId: String): Result<Snapshot> {
        return try {
            val snapshot = repository.getSnapshot(municipioId)
            Result.success(snapshot)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
