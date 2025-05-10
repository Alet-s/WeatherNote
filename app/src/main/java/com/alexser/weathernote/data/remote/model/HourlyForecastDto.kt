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
    val temperatura: List<HourlyTempDto>,
    val sensTermica: List<HourlyTempDto>?,
    val precipitacion: List<HourlyTempDto>?,
    val nieve: List<HourlyTempDto>?,
    val humedadRelativa: List<HourlyTempDto>?,
    val vientoAndRachaMax: List<HourlyWindDto>?
)

data class HourlyWindDto(
    val periodo: String,
    val direccion: List<String> = emptyList(),
    val velocidad: List<String> = emptyList(),
    val value: String? = null
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

data class HourlyForecastFullItem(
    val hour: String,              // "14" for 14:00
    val temperature: Int?,         // °C
    val feelsLike: Int?,           // Sensación térmica
    val condition: String?,        // Estado del cielo (descripcion)
    val precipitation: Double?,    // mm de lluvia
    val snow: Double?,             // mm de nieve
    val humidity: Int?,            // %
    val windDirection: String?,   // Dirección del viento
    val windSpeed: Int?,          // Velocidad del viento (km/h)
    val maxGust: Int?              // Racha máxima (km/h)
)


