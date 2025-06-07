package com.alexser.weathernote.domain.model

//TODO: para futuras implementaciones
data class HourlyForecastItem(
    val hour: String, // e.g., "14:00"
    val temp: Int,
    val condition: String
)