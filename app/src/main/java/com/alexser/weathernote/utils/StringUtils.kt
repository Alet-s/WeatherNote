package com.alexser.weathernote.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatMunicipioName(rawName: String): String {
    val mainPart = rawName.split("/").first().trim()
    val parts = mainPart.split(", ").map { it.trim() }

    val reordered = if (parts.size == 2) {
        val article = parts[1].replaceFirstChar { it.titlecase() }
        "$article ${parts[0]}"
    } else {
        parts[0]
    }

    return reordered.lowercase()
        .split(" ")
        .joinToString(" ") { word ->
            if (word.isNotEmpty()) word.replaceFirstChar { it.titlecase() } else word
        }
}

fun formatDateAsDayAndMonth(dateStr: String): String {
    return try {
        val cleaned = dateStr.take(10) // extrae "YYYY-MM-DD"
        val date = LocalDate.parse(cleaned)
        val formatter = DateTimeFormatter.ofPattern("d MMMM", Locale("es", "ES"))
        date.format(formatter)
    } catch (e: Exception) {
        dateStr
    }
}

