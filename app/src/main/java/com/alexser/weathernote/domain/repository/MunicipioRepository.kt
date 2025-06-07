package com.alexser.weathernote.domain.repository

import com.alexser.weathernote.domain.model.SavedMunicipio
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio para la gestión de municipios guardados por el usuario.
 */
interface MunicipioRepository {

    /**
     * Obtiene un flujo que emite la lista de todos los municipios guardados.
     *
     * @return [Flow] que emite listas de [SavedMunicipio].
     */
    fun getAll(): Flow<List<SavedMunicipio>>

    /**
     * Añade un municipio a la lista de municipios guardados.
     *
     * @param municipio Municipio a añadir.
     */
    suspend fun add(municipio: SavedMunicipio)

    /**
     * Elimina un municipio guardado dado su identificador.
     *
     * @param id Identificador único del municipio a eliminar.
     */
    suspend fun remove(id: String)

    /**
     * Obtiene una lista estática con todos los municipios guardados.
     *
     * @return Lista de [SavedMunicipio].
     */
    suspend fun getAllSavedMunicipios(): List<SavedMunicipio>
}
