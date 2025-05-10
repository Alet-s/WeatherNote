package com.alexser.weathernote.data.remote.mapper

import com.alexser.weathernote.data.remote.model.HourlyForecastDto
import com.alexser.weathernote.data.remote.model.HourlyForecastFullItem
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

fun HourlyForecastDto.toHourlyForecastFullItems(): List<HourlyForecastFullItem> {
    return prediccion.dia.flatMap { dia ->
        val temps = dia.temperatura.associateBy { it.periodo }
        val feels = dia.sensTermica?.associateBy { it.periodo } ?: emptyMap()
        val skies = dia.estadoCielo.associateBy { it.periodo }
        val precs = dia.precipitacion?.associateBy { it.periodo } ?: emptyMap()
        val snows = dia.nieve?.associateBy { it.periodo } ?: emptyMap()
        val hums = dia.humedadRelativa?.associateBy { it.periodo } ?: emptyMap()
        val winds = dia.vientoAndRachaMax?.filter { it.direccion != null && it.direccion.isNotEmpty() }?.associateBy { it.periodo } ?: emptyMap()
        val gusts = dia.vientoAndRachaMax?.filter { it.value != null }?.associateBy { it.periodo } ?: emptyMap()

        temps.mapNotNull { (hour, tempDto) ->
            HourlyForecastFullItem(
                hour = hour,
                temperature = tempDto.value.toIntOrNull(),
                feelsLike = feels[hour]?.value?.toIntOrNull(),
                condition = skies[hour]?.descripcion,
                precipitation = precs[hour]?.value?.toDoubleOrNull(),
                snow = snows[hour]?.value?.toDoubleOrNull(),
                humidity = hums[hour]?.value?.toIntOrNull(),
                windDirection = winds[hour]?.direccion?.firstOrNull(),
                windSpeed = winds[hour]?.velocidad?.firstOrNull()?.toIntOrNull(),
                maxGust = gusts[hour]?.value?.toIntOrNull()
            )
        }
    }
}

