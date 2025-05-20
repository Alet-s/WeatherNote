package com.alexser.weathernote.domain.model

enum class SnapshotRetentionOption(val maxSnapshots: Int?) {
    KEEP_15(15 * 24),     // 15 días = 360 snapshots
    KEEP_31(31 * 24),     // 1 mes = 744 aprox snapshots
    KEEP_62(62 * 24),     // 2 meses
    KEEP_93(93 * 24),     // 3 meses
    KEEP_186(186 * 24),   // 6 meses
    KEEP_365(365 * 24),   // 1 año = 8,760
    KEEP_ALL(null)        // Retención total de snapshots
}