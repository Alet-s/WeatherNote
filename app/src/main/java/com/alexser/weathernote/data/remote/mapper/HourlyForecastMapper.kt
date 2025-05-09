package com.alexser.weathernote.data.remote.mapper

import com.alexser.weathernote.data.remote.model.HourlyForecastDto
import com.alexser.weathernote.data.remote.model.HourlyForecastItem


fun HourlyForecastDto.toHourlyForecastItems(): List<HourlyForecastItem> {
    return prediccion.dia.flatMap { dia ->
        dia.temperatura.mapNotNull { tempEntry ->
            val condition = dia.estadoCielo.find { it.periodo == tempEntry.periodo }?.descripcion ?: "Desconocido"

            // Only map if temperature is a valid integer
            tempEntry.value.toIntOrNull()?.let { temperature ->
                HourlyForecastItem(
                    hour = tempEntry.periodo,
                    temperature = temperature,
                    condition = condition
                )
            }
        }
    }
}
