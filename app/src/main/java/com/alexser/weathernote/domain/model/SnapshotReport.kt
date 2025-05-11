package com.alexser.weathernote.domain.model

data class SnapshotReport(
    val timestamp: String,         // ISO format
    val municipioId: String,
    val municipioName: String,
    val temperature: Int?,
    val feelsLike: Int?,
    val condition: String?,
    val precipitation: Double?,
    val snow: Double?,
    val humidity: Int?,
    val windDirection: String?,
    val windSpeed: Int?,
    val maxGust: Int?,
    val userNote: String? = null   // Optional user text
)
