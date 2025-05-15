package com.alexser.weathernote.domain.model

data class SnapshotReport(
    val reportId: String = "",
    val timestamp: String = "",
    val municipioId: String = "",
    val municipioName: String = "",
    val temperature: Int? = null,
    val feelsLike: Int? = null,
    val condition: String? = null,
    val precipitation: Double? = null,
    val snow: Double? = null,
    val humidity: Int? = null,
    val windDirection: String? = null,
    val windSpeed: Int? = null,
    val maxGust: Int? = null,
    val userNote: String? = null
)
