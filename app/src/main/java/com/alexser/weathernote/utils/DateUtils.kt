package com.alexser.weathernote.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun formatIsoDateAsSpanish(isoDate: String): String {
    return try {
        val parsedDate = LocalDate.parse(isoDate.substringBefore("T"))
        parsedDate.format(DateTimeFormatter.ofPattern("dd 'de' MMMM yyyy"))
    } catch (e: Exception) {
        isoDate // fallback
    }
}

