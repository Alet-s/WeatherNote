package com.alexser.weathernote.domain.model

enum class SnapshotRetentionOption(val maxSnapshots: Int?) {
    KEEP_15(15 * 24),     // 15 days = 360 snapshots
    KEEP_31(31 * 24),     // 1 month ≈ 744 snapshots
    KEEP_62(62 * 24),     // 2 months
    KEEP_93(93 * 24),     // 3 months
    KEEP_186(186 * 24),   // 6 months
    KEEP_365(365 * 24),   // 1 year = 8,760
    KEEP_ALL(null)        // Infinite
}