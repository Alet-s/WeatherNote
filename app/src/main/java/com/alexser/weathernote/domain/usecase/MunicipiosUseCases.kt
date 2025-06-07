package com.alexser.weathernote.domain.usecase

import com.alexser.weathernote.data.firebase.FirestoreDataSource
import com.alexser.weathernote.domain.model.SavedMunicipio
import com.alexser.weathernote.domain.repository.MunicipioRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso para obtener el flujo de la lista de municipios guardados.
 *
 * Retorna un [Flow] que emite la lista actualizada de municipios guardados.
 *
 * @property repository Repositorio para acceder a los municipios guardados.
 */
class GetSavedMunicipiosUseCase @Inject constructor(
    private val repository: MunicipioRepository
) {
    operator fun invoke(): Flow<List<SavedMunicipio>> = repository.getAll()
}

/**
 * Caso de uso para añadir un municipio a la lista de municipios guardados.
 *
 * @property repository Repositorio para acceder y modificar la lista de municipios guardados.
 */
class AddMunicipioUseCase @Inject constructor(
    private val repository: MunicipioRepository
) {
    suspend operator fun invoke(municipio: SavedMunicipio) {
        repository.add(municipio)
    }
}

/**
 * Caso de uso para eliminar un municipio de la lista de municipios guardados.
 *
 * @property repository Repositorio para acceder y modificar la lista de municipios guardados.
 */
class RemoveMunicipioUseCase @Inject constructor(
    private val repository: MunicipioRepository
) {
    suspend operator fun invoke(municipio: SavedMunicipio) {
        repository.remove(municipio.id)
    }
}

/**
 * Caso de uso para buscar el código de un municipio dado su nombre.
 *
 * Realiza la búsqueda en Firestore mediante [FirestoreDataSource].
 *
 * @property firestoreDataSource Fuente de datos para Firestore.
 */
class FindMunicipioByNameUseCase @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource
) {
    suspend operator fun invoke(name: String): String? {
        return firestoreDataSource.getMunicipioCodeByName(name)
    }
}
