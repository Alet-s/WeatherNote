package com.alexser.weathernote.presentation.screens.municipios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexser.weathernote.domain.model.SavedMunicipio
import com.alexser.weathernote.domain.model.Snapshot
import com.alexser.weathernote.domain.usecase.AddMunicipioUseCase
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
    private val getSnapshotUseCase: GetSnapshotUseCase
) : ViewModel() {

    private val _municipios = MutableStateFlow<List<SavedMunicipio>>(emptyList())
    val municipios: StateFlow<List<SavedMunicipio>> = _municipios

    private val _snapshots = MutableStateFlow<Map<String, Snapshot?>>(emptyMap())
    val snapshots: StateFlow<Map<String, Snapshot?>> = _snapshots

    init {
        loadMunicipios()
    }

    fun loadMunicipios() {
        viewModelScope.launch {
            getSavedMunicipiosUseCase().collect { savedList ->
                _municipios.value = savedList
                loadSnapshots(savedList)
            }
        }
    }


    private fun loadSnapshots(municipios: List<SavedMunicipio>) {
        municipios.forEach { municipio ->
            viewModelScope.launch {
                val snapshot = getSnapshotUseCase(municipio.id).getOrNull()
                _snapshots.update { it + (municipio.id to snapshot) }
            }
        }
    }

    fun addMunicipio(id: String, name: String) {
        viewModelScope.launch {
            val newMunicipio = SavedMunicipio(id = id, nombre = name)
            addMunicipioUseCase(newMunicipio)
            loadMunicipios()
        }
    }

    fun removeMunicipio(id: String) {
        viewModelScope.launch {
            val toRemove = _municipios.value.find { it.id == id } ?: return@launch
            removeMunicipioUseCase(toRemove)
            loadMunicipios()
        }
    }
}
