package com.alexser.weathernote.domain.model

/**
 * Modelo de dominio que representa una predicción meteorológica básica para una ciudad.
 *
 * @property cityId Identificador único del municipio o ciudad.
 * @property city Nombre de la ciudad o municipio.
 * @property date Fecha de la predicción en formato String.
 * @property maxTemp Temperatura máxima prevista para la fecha.
 * @property minTemp Temperatura mínima prevista para la fecha.
 * @property condition Descripción del estado del tiempo (por ejemplo, "soleado", "lluvioso", etc.).
 */
data class BasicWeatherForecast(
    val cityId: String,
    val city: String,
    val date: String,
    val maxTemp: Float,
    val minTemp: Float,
    val condition: String
)
