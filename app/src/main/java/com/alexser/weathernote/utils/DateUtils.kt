package com.alexser.weathernote.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatIsoDateAsSpanish(isoDate: String): String {
    return try {
        val parsedDate = LocalDate.parse(isoDate.substringBefore("T"))
        parsedDate.format(DateTimeFormatter.ofPattern("dd 'de' MMMM yyyy"))
    } catch (e: Exception) {
        isoDate // fallback
    }
}

fun String.toFormattedForecastLabel(): String {
    val today = LocalDate.now()
    val forecastDate = LocalDate.parse(this)

    return when (forecastDate) {
        today.plusDays(1) -> "Mañana"
        today.plusDays(2) -> "Pasado mañana"
        else -> {
            val formatter = DateTimeFormatter.ofPattern("d-MMMM", Locale("es", "ES"))
            forecastDate.format(formatter)
        }
    }
}