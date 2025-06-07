package com.alexser.weathernote.domain.model

/**
 * Modelo que representa la predicción meteorológica diaria para un municipio.
 *
 * @property date Fecha de la predicción en formato String (por ejemplo, "2025-06-07").
 * @property maxTemp Temperatura máxima esperada para el día (en grados Celsius).
 * @property minTemp Temperatura mínima esperada para el día (en grados Celsius).
 * @property condition Descripción textual del estado del cielo o condición meteorológica (por ejemplo, "Soleado", "Nublado").
 */
data class DailyForecast(
    val date: String,
    val maxTemp: Int,
    val minTemp: Int,
    val condition: String
)
