package com.alexser.weathernote.data.remote.model

data class HourlyForecastDto(
    val nombre: String,
    val prediccion: HourlyPrediccionDto
)

data class HourlyPrediccionDto(
    val dia: List<HourlyDiaDto>
)

data class HourlyDiaDto(
    val fecha: String,
    val estadoCielo: List<HourlyEstadoCieloDto>,
    val temperatura: List<HourlyTempDto>
)

data class HourlyEstadoCieloDto(
    val periodo: String,
    val descripcion: String?
)

data class HourlyTempDto(
    val periodo: String,
    val value: String
)

data class HourlyForecastItem(
    val hour: String,        // "14" for 14:00
    val temperature: Int,    // 18
    val condition: String    // "Cubierto"
)
