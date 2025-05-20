package com.alexser.weathernote.domain.model

//TODO: BORRAR
enum class SnapshotFrequency {
    MANUAL,
    HOURLY,
    EVERY_2H,
    EVERY_6H,
    EVERY_12H,
    DAILY;

    fun toDisplayName(): String {
        return when (this) {
            MANUAL -> "Manual"
            HOURLY -> "Every hour"
            EVERY_2H -> "Every 2 hours"
            EVERY_6H -> "Every 6 hours"
            EVERY_12H -> "Every 12 hours"
            DAILY -> "Daily"
        }
    }
}
