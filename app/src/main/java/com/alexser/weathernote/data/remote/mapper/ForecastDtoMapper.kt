package com.alexser.weathernote.data.remote.mapper

import DiaDto
import com.alexser.weathernote.domain.model.DailyForecast

/**
 * Convierte un objeto [DiaDto] (modelo recibido del backend o API)
 * en un objeto del dominio [DailyForecast] utilizado por la aplicación.
 *
 * La conversión extrae la fecha, temperatura máxima y mínima además de una descripción
 * legible del estado del cielo.
 *
 * Si no se encuentra una descripción válida en la lista de `estadoCielo`,
 * se devuelve la cadena `"Desconocido"` como valor por defecto.
 *
 * @return Una instancia de [DailyForecast] con los datos extraídos.
 */
fun DiaDto.toDailyForecast(): DailyForecast {
    val condition =
        estadoCielo.firstOrNull { !it.descripcion.isNullOrBlank() }?.descripcion ?: "Desconocido"
    return DailyForecast(
        date = fecha,
        maxTemp = temperatura.maxima,
        minTemp = temperatura.minima,
        condition = condition
    )
}
