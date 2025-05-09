package com.alexser.weathernote.presentation.screens.municipios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexser.weathernote.data.firebase.MunicipioSyncService
import com.alexser.weathernote.domain.model.SavedMunicipio
import com.alexser.weathernote.domain.model.Snapshot
import com.alexser.weathernote.domain.usecase.AddMunicipioUseCase
import com.alexser.weathernote.domain.usecase.FindMunicipioByNameUseCase
import com.alexser.weathernote.domain.usecase.GetSavedMunicipiosUseCase
import com.alexser.weathernote.domain.usecase.GetSnapshotUseCase
import com.alexser.weathernote.domain.usecase.RemoveMunicipioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MunicipiosScreenViewModel @Inject constructor(
    private val getSavedMunicipiosUseCase: GetSavedMunicipiosUseCase,
    private val addMunicipioUseCase: AddMunicipioUseCase,
    private val removeMunicipioUseCase: RemoveMunicipioUseCase,
    private val getSnapshotUseCase: GetSnapshotUseCase,
    private val findMunicipioByNameUseCase: FindMunicipioByNameUseCase,
    private val syncService: MunicipioSyncService
) : ViewModel() {

    private val _municipios = MutableStateFlow<List<SavedMunicipio>>(emptyList())
    val municipios: StateFlow<List<SavedMunicipio>> = _municipios

    private val _snapshots = MutableStateFlow<Map<String, Snapshot?>>(emptyMap())
    val snapshots: StateFlow<Map<String, Snapshot?>> = _snapshots

    private val _syncSuccess = MutableStateFlow<Boolean?>(null)
    val syncSuccess: StateFlow<Boolean?> = _syncSuccess

    init {
        viewModelScope.launch {
            getSavedMunicipiosUseCase().collect { savedList ->
                _municipios.value = savedList
                savedList.forEach { municipio ->
                    if (!_snapshots.value.containsKey(municipio.id)) {
                        val snapshot = getSnapshotUseCase(municipio.id).getOrNull()
                        _snapshots.update { it + (municipio.id to snapshot) }
                    }
                }
            }
        }
    }

    fun addMunicipioByName(name: String) {
        viewModelScope.launch {
            val id = findMunicipioByNameUseCase(name)
            if (id != null) {
                val alreadyExists = _municipios.value.any { it.id == id }
                if (!alreadyExists) {
                    val newMunicipio = SavedMunicipio(id = id, nombre = name)
                    addMunicipioUseCase(newMunicipio)

                    val snapshot = getSnapshotUseCase(id).getOrNull()
                    _snapshots.update { it + (id to snapshot) }

                    syncMunicipiosToFirestore()
                }
            }
        }
    }

    fun removeMunicipio(id: String) {
        viewModelScope.launch {
            val toRemove = _municipios.value.find { it.id == id } ?: return@launch
            removeMunicipioUseCase(toRemove)
            _municipios.update { it.filterNot { it.id == id } }
            _snapshots.update { it - id }

            syncMunicipiosToFirestore()
        }
    }

    fun syncMunicipiosToFirestore() {
        viewModelScope.launch {
            try {
                syncService.uploadMunicipios(_municipios.value)
                _syncSuccess.value = true
            } catch (e: Exception) {
                _syncSuccess.value = false
            }
        }
    }

    fun resetSyncStatus() {
        _syncSuccess.value = null
    }
}
