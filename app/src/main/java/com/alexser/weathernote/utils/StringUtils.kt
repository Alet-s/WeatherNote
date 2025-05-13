package com.alexser.weathernote.utils

import java.text.Normalizer

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
