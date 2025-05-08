package com.alexser.weathernote.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDate.formatAsSpanishDate(): String {
    val formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM", Locale("es", "ES"))
    return this.format(formatter)
}


fun formatIsoDateAsSpanish(isoDate: String): String {
    return try {
        val parsedDate = LocalDate.parse(isoDate.substringBefore("T"))
        parsedDate.format(DateTimeFormatter.ofPattern("dd 'de' MMMM yyyy"))
    } catch (e: Exception) {
        isoDate // fallback
    }
}
