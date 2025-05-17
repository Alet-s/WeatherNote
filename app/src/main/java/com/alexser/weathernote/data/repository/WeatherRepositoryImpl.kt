package com.alexser.weathernote.data.repository

import com.alexser.weathernote.data.remote.AemetService
import com.alexser.weathernote.data.remote.model.HourlyForecastDto
import com.alexser.weathernote.domain.model.BasicWeatherForecast
import com.alexser.weathernote.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val aemetService: AemetService
) : WeatherRepository {

    override suspend fun getSnapshot(municipioId: String): BasicWeatherForecast {
        return aemetService.getSnapshot(municipioId)
    }

    override suspend fun getHourlyForecast(municipioId: String): List<HourlyForecastDto> {
        return aemetService.getHourlyForecast(municipioId)
    }
}
