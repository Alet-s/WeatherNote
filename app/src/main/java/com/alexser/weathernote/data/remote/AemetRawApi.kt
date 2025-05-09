package com.alexser.weathernote.data.remote

import ForecastDto
import com.alexser.weathernote.data.remote.model.HourlyForecastDto
import retrofit2.http.GET
import retrofit2.http.Url

interface AemetRawApi {
    @GET
    suspend fun getDailyForecast(@Url forecastUrl: String): List<ForecastDto>

    @GET
    suspend fun getHourlyForecast(@Url url: String): List<HourlyForecastDto>

}
