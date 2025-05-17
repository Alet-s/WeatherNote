package com.alexser.weathernote.data.repository

import com.alexser.weathernote.domain.model.SavedMunicipio
import com.alexser.weathernote.domain.repository.MunicipioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MunicipioRepositoryImpl @Inject constructor() : MunicipioRepository {

    private val municipios = MutableStateFlow<List<SavedMunicipio>>(emptyList())

    override fun getAll(): Flow<List<SavedMunicipio>> = municipios

    override suspend fun add(municipio: SavedMunicipio) {
        municipios.value = municipios.value + municipio
    }

    override suspend fun remove(id: String) {
        municipios.value = municipios.value.filterNot { it.id == id }
    }

    override suspend fun getAllSavedMunicipios(): List<SavedMunicipio> {
        return municipios.value
    }
}
