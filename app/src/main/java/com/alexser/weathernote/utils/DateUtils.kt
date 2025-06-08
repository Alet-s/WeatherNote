package com.alexser.weathernote.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Formatea una fecha en formato ISO 8601 a una cadena con formato en español.
 *
 * La función espera que la fecha ISO sea una cadena que contenga una parte de fecha
 * en formato "yyyy-MM-dd" seguida opcionalmente por una 'T' y la hora (por ejemplo, "2023-06-08T15:30:00").
 * Extrae solo la parte de la fecha y la formatea como "dd 'de' MMMM yyyy" (ejemplo: "08 de junio 2023").
 *
 * @param isoDate La fecha en formato ISO 8601, como "2023-06-08T15:30:00".
 * @return La fecha formateada en español, o la cadena original si ocurre algún error de parseo.
 */
fun formatIsoDateAsSpanish(isoDate: String): String {
    return try {
        val parsedDate = LocalDate.parse(isoDate.substringBefore("T"))
        parsedDate.format(DateTimeFormatter.ofPattern("dd 'de' MMMM yyyy"))
    } catch (e: Exception) {
        isoDate // fallback
    }
}

