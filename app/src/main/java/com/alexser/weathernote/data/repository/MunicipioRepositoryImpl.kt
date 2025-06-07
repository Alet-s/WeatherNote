package com.alexser.weathernote.data.repository

import com.alexser.weathernote.domain.model.SavedMunicipio
import com.alexser.weathernote.domain.repository.MunicipioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación en memoria de [MunicipioRepository] que gestiona una lista de municipios guardados.
 *
 * Esta clase está anotada con [Singleton], lo que garantiza que se mantenga un único estado
 * de municipios a lo largo del ciclo de vida de la aplicación mientras el proceso esté activo.
 */
@Singleton
class MunicipioRepositoryImpl @Inject constructor() : MunicipioRepository {

    /** Flujo de estados con la lista actual de municipios guardados. */
    private val municipios = MutableStateFlow<List<SavedMunicipio>>(emptyList())

    /**
     * Devuelve un [Flow] que emite actualizaciones en tiempo real de la lista de municipios guardados.
     */
    override fun getAll(): Flow<List<SavedMunicipio>> = municipios

    /**
     * Añade un nuevo municipio a la lista de guardados.
     *
     * @param municipio Municipio a guardar.
     */
    override suspend fun add(municipio: SavedMunicipio) {
        municipios.value = municipios.value + municipio
    }

    /**
     * Elimina un municipio de la lista mediante su ID.
     *
     * @param id Identificador del municipio a eliminar.
     */
    override suspend fun remove(id: String) {
        municipios.value = municipios.value.filterNot { it.id == id }
    }

    /**
     * Devuelve la lista actual de municipios guardados como una colección inmutable.
     *
     * @return Lista de [SavedMunicipio].
     */
    override suspend fun getAllSavedMunicipios(): List<SavedMunicipio> {
        return municipios.value
    }
}
