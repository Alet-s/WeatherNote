package com.alexser.weathernote.domain.usecase

import com.alexser.weathernote.data.firebase.FirestoreDataSource
import com.alexser.weathernote.domain.model.SavedMunicipio
import com.alexser.weathernote.domain.repository.MunicipioRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedMunicipiosUseCase @Inject constructor(
    private val repository: MunicipioRepository
) {
    operator fun invoke(): Flow<List<SavedMunicipio>> = repository.getAll()
}


class AddMunicipioUseCase @Inject constructor(
    private val repository: MunicipioRepository
) {
    suspend operator fun invoke(municipio: SavedMunicipio) {
        repository.add(municipio)
    }
}

class RemoveMunicipioUseCase @Inject constructor(
    private val repository: MunicipioRepository
) {
    suspend operator fun invoke(municipio: SavedMunicipio) {
        repository.remove(municipio.id)
    }
}

class FindMunicipioByNameUseCase @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource
) {
    suspend operator fun invoke(name: String): String? {
        return firestoreDataSource.getMunicipioCodeByName(name)
    }
}

//class SuggestMunicipiosUseCase @Inject constructor(
//    private val firestoreDataSource: FirestoreDataSource
//) {
//    suspend operator fun invoke(prefix: String): List<String> {
//        return firestoreDataSource.suggestMunicipiosByPrefix(prefix)
//    }
//}

