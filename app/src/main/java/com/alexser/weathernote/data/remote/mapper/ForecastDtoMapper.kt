package com.alexser.weathernote.data.remote.mapper

import DiaDto
import com.alexser.weathernote.domain.model.DailyForecast

fun DiaDto.toDailyForecast(): DailyForecast {
    val condition = estadoCielo.firstOrNull { !it.descripcion.isNullOrBlank() }?.descripcion ?: "Desconocido"
    return DailyForecast(
        date = fecha,
        maxTemp = temperatura.maxima,
        minTemp = temperatura.minima,
        condition = condition
    )
}

