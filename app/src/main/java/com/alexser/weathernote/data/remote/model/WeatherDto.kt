package com.alexser.weathernote.data.remote.model

data class WeatherDto(
    val temperature: Float,
    val description: String,
    val city: String
)
//TODO: cambiar los campos por aquellos devueltos por aemet