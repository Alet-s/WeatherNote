package com.alexser.weathernote.data.repository

import com.alexser.weathernote.data.remote.AemetService
import com.alexser.weathernote.domain.model.Snapshot
import com.alexser.weathernote.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val aemetService: AemetService
) : WeatherRepository {

    override suspend fun getSnapshot(municipioId: String): Snapshot {
        return aemetService.getSnapshot(municipioId)
    }
}
