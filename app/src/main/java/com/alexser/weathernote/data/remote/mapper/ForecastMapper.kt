package com.alexser.weathernote.data.remote.mapper

import ForecastDto
import com.alexser.weathernote.domain.model.Snapshot

fun ForecastDto.toSnapshot(): Snapshot {
    val dia = prediccion.dia.firstOrNull()
    val estado = dia?.estadoCielo?.firstOrNull { !it.descripcion.isNullOrBlank() }?.descripcion ?: "Desconocido"
    return Snapshot(
        city = nombre,
        date = dia?.fecha ?: "",
        maxTemp = dia?.temperatura?.maxima?.toFloat() ?: 0f,
        minTemp = dia?.temperatura?.minima?.toFloat() ?: 0f,
        condition = estado
    )
}
