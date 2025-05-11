package com.alexser.weathernote.domain.repository

import com.alexser.weathernote.domain.model.SnapshotFrequency

interface SnapshotConfigRepository {
    suspend fun getSnapshotFrequency(municipioId: String): SnapshotFrequency
    suspend fun saveSnapshotFrequency(municipioId: String, frequency: SnapshotFrequency)
}
