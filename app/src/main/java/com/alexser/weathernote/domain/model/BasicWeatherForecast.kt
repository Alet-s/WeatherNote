package com.alexser.weathernote.domain.model

data class BasicWeatherForecast(
    val cityId: String,
    val city: String,
    val date: String,
    val maxTemp: Float,
    val minTemp: Float,
    val condition: String
)
