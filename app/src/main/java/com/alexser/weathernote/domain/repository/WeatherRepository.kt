package com.alexser.weathernote.domain.repository


import com.alexser.weathernote.domain.model.Snapshot

interface WeatherRepository {
    suspend fun getSnapshot(municipioId: String): Snapshot
}
