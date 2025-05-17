package com.alexser.weathernote.domain.repository

import com.alexser.weathernote.domain.model.SavedMunicipio
import kotlinx.coroutines.flow.Flow

interface MunicipioRepository {
    fun getAll(): Flow<List<SavedMunicipio>>
    suspend fun add(municipio: SavedMunicipio)
    suspend fun remove(id: String)
    suspend fun getAllSavedMunicipios(): List<SavedMunicipio>
}
