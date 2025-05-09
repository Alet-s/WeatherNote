package com.alexser.weathernote.data.remote

import com.alexser.weathernote.domain.model.Snapshot
import com.alexser.weathernote.utils.Constants.AEMET_API_KEY
import com.alexser.weathernote.data.remote.mapper.toSnapshot
import com.alexser.weathernote.data.remote.model.HourlyForecastDto
import javax.inject.Inject
import kotlin.collections.first

class AemetService @Inject constructor(
    private val aemetApi: AemetApi,
    private val rawApi: AemetRawApi
) {
    suspend fun getSnapshot(municipioId: String): Snapshot {
        val meta = aemetApi.getForecastMetadata(municipioId, AEMET_API_KEY)
        val fullData = rawApi.getDailyForecast(meta.datos)
        return fullData.first().toSnapshot()
    }

    suspend fun getHourlyForecast(municipioId: String): List<HourlyForecastDto> {
        val meta = aemetApi.getHourlyForecastMetadata(municipioId, AEMET_API_KEY)
        return rawApi.getHourlyForecast(meta.datos)
    }

}
