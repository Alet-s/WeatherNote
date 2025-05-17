package com.alexser.weathernote.domain.model

enum class SnapshotRetentionOption(val maxSnapshots: Int?) {
    KEEP_15(15),
    KEEP_31(31),
    KEEP_62(62),
    KEEP_93(93),
    KEEP_186(186),
    KEEP_365(365),
    KEEP_ALL(null) // null = infinite
}
