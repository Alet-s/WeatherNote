package com.alexser.weathernote.domain.model

data class Snapshot(
    val city: String,
    val date: String,
    val maxTemp: Float,
    val minTemp: Float,
    val condition: String
)

//Enum con los tipos de tiempo atmosférico
enum class WeatherType {
    SUNNY, CLOUDY, RAINY, STORMY, SNOWY
}
