package com.alexser.weathernote.domain.model

data class DailyForecast(
    val date: String,
    val maxTemp: Int,
    val minTemp: Int,
    val condition: String
)
