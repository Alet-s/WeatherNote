package com.alexser.weathernote.data.remote.mapper

import com.alexser.weathernote.domain.model.SnapshotReport
import com.alexser.weathernote.data.remote.model.HourlyForecastFullItem
import java.time.LocalDateTime

fun HourlyForecastFullItem.toSnapshotReport(
    municipioId: String,
    municipioName: String,
    //TODO: cambiar este dateT por date
    dateT: String, // "2025-05-11"
    userNote: String? = null
): SnapshotReport {
    val timestamp = try {
        val parsed = LocalDateTime.parse("$dateT${hour.padStart(2, '0')}:00")
        parsed.toString()  // ISO format
    } catch (e: Exception) {
        "$dateT${hour.padStart(2, '0')}:00" // fallback
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
