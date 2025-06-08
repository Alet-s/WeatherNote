package com.alexser.weathernote.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Formatea el nombre de un municipio dado en bruto.
 *
 * La función:
 * - Divide el nombre original por "/", tomando la primera parte.
 * - Divide esa parte por ", " para detectar formatos como "Nombre, el".
 * - Si encuentra dos partes, las reordena para que el artículo (ej. "El") quede delante.
 * - Convierte el resultado a minúsculas y luego capitaliza la primera letra de cada palabra.
 *
 * Ejemplo: "Madrid / algo" -> "Madrid"
 *          "Playa, la" -> "La Playa"
 *
 * @param rawName Nombre original del municipio sin formato.
 * @return Nombre formateado con capitalización adecuada y reordenado si es necesario.
 */
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

/**
 * Formatea una cadena de fecha ISO (YYYY-MM-DD o con tiempo) para mostrar solo
 * el día y el mes en español, en formato "d MMMM" (ej. "5 junio").
 *
 * Si la fecha no puede ser parseada, devuelve la cadena original.
 *
 * @param dateStr Fecha en formato ISO 8601 (ej. "2023-06-05T12:34:56").
 * @return Fecha formateada como "día mes" en español o la cadena original si falla el parseo.
 */
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

