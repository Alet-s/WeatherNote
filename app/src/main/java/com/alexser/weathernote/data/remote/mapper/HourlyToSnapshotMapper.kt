package com.alexser.weathernote.data.remote.mapper

import com.alexser.weathernote.domain.model.SnapshotReport
import com.alexser.weathernote.data.remote.model.HourlyForecastFullItem
import java.time.LocalDateTime

/**
 * Convierte un [HourlyForecastFullItem] en un objeto de dominio [SnapshotReport].
 *
 * Esta función transforma los datos meteorológicos por hora en un snapshot completo,
 * agregando metadatos como el nombre del municipio, su ID, la fecha y una nota opcional del usuario.
 *
 * El campo `timestamp` se genera combinando la fecha y la hora del pronóstico en formato ISO 8601.
 *
 * @param municipioId ID único del municipio.
 * @param municipioName Nombre del municipio.
 * @param date Fecha del pronóstico (en formato ISO, por ejemplo "2025-05-11").
 * @param userNote Nota opcional del usuario.
 * @return Instancia de [SnapshotReport] lista para persistencia o visualización.
 */
fun HourlyForecastFullItem.toSnapshotReport(
    municipioId: String,
    municipioName: String,
    date: String, // ISO date like "2025-05-11"
    userNote: String? = null
): SnapshotReport {
    val timestamp = try {
        val parsed = LocalDateTime.parse("${date}T${hour.padStart(2, '0')}:00")
        parsed.toString()  // ISO 8601 format
    } catch (e: Exception) {
        "${date}T${hour.padStart(2, '0')}:00" // fallback, still ISO-ish
    }

    return SnapshotReport(
        timestamp = timestamp,
        municipioId = municipioId,
        municipioName = municipioName,
        temperature = temperature,
        feelsLike = feelsLike,
        condition = condition,
        precipitation = precipitation,
        snow = snow,
        humidity = humidity,
        windDirection = windDirection,
        windSpeed = windSpeed,
        maxGust = maxGust,
        userNote = userNote
    )
}

