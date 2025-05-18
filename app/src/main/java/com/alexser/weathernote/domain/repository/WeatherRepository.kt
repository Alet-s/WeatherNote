package com.alexser.weathernote.domain.repository


import com.alexser.weathernote.data.remote.model.HourlyForecastDto
import com.alexser.weathernote.domain.model.BasicWeatherForecast

interface WeatherRepository {
    suspend fun getBasicWeatherForecast(municipioId: String): BasicWeatherForecast
    suspend fun getHourlyForecast(municipioId: String): List<HourlyForecastDto>
}
