package com.alexser.weathernote.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDate.formatAsSpanishDate(): String {
    val formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM", Locale("es", "ES"))
    return this.format(formatter)
}
