package com.alexser.weathernote.domain.model

import java.time.LocalDate

data class WeatherReport(
    val city: String,
    val date: LocalDate,
    val temperature: Float,
    val weatherType: WeatherType,  // An enum or sealed class
    val isFavorite: Boolean = false
)
//Enum con los tipos de tiempo atmosférico
enum class WeatherType {
    SUNNY, CLOUDY, RAINY, STORMY, SNOWY
}
