package com.alexser.weathernote.presentation.screens.snapshot

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alexser.weathernote.data.local.SnapshotPreferences
import com.alexser.weathernote.domain.model.SavedMunicipio
import com.alexser.weathernote.domain.model.SnapshotRetentionOption
import com.alexser.weathernote.domain.repository.MunicipioRepository
import com.alexser.weathernote.domain.repository.SnapshotReportRepository
import com.alexser.weathernote.utils.SnapshotCleanupManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SnapshotConfigScreenViewModel @Inject constructor(
    application: Application,
    private val snapshotRepository: SnapshotReportRepository,
    private val municipioRepository: MunicipioRepository
) : AndroidViewModel(application) {

    private val prefs = SnapshotPreferences(application.applicationContext)

    private val _selectedOption =
        MutableStateFlow(prefs.getSnapshotRetention() ?: SnapshotRetentionOption.KEEP_ALL)
    val selectedOption: StateFlow<SnapshotRetentionOption> = _selectedOption

    private val _allMunicipios = MutableStateFlow<List<SavedMunicipio>>(emptyList())
    val allMunicipios: StateFlow<List<SavedMunicipio>> = _allMunicipios

    private val _selectedMunicipioIds = MutableStateFlow<Set<String>>(emptySet())
    val selectedMunicipioIds: StateFlow<Set<String>> = _selectedMunicipioIds

    init {
        viewModelScope.launch {
            _allMunicipios.value = municipioRepository.getAllSavedMunicipios()
        }
    }

    fun onOptionSelected(option: SnapshotRetentionOption) {
        _selectedOption.value = option
    }

    fun toggleMunicipio(id: String) {
        _selectedMunicipioIds.value = _selectedMunicipioIds.value.toMutableSet().also { set ->
            if (set.contains(id)) set.remove(id) else set.add(id)
        }
    }

    fun saveOptionToSelectedMunicipios() {
        val option = _selectedOption.value
        _selectedMunicipioIds.value.forEach { municipioId ->
            prefs.setRetentionForMunicipio(municipioId, option)
        }
    }

    fun enforceCleanup() {
        viewModelScope.launch(Dispatchers.IO) {
            SnapshotCleanupManager(getApplication(), snapshotRepository).runCleanupIfNeeded()
        }
    }
}
