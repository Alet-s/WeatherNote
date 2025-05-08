package com.alexser.weathernote.data.remote

import ForecastDto
import retrofit2.http.GET
import retrofit2.http.Url

interface AemetRawApi {
    @GET
    suspend fun getDailyForecast(@Url forecastUrl: String): List<ForecastDto>
}
