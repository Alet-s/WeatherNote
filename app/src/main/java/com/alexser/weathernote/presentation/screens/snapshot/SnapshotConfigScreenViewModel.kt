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

/**
 * ViewModel para la pantalla de configuración de retención de snapshots.
 *
 * Gestiona el estado de la opción de retención seleccionada,
 * la lista de municipios guardados y qué municipios han sido seleccionados para aplicar la retención.
 *
 * Además, permite guardar la configuración en preferencias y forzar la limpieza de snapshots antiguos.
 *
 * @property application Contexto de aplicación para acceso a preferencias y recursos.
 * @property snapshotRepository Repositorio para manejar snapshots meteorológicos.
 * @property municipioRepository Repositorio para obtener municipios guardados.
 */
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

    /**
     * Actualiza la opción de retención seleccionada.
     *
     * @param option Nueva opción seleccionada.
     */
    fun onOptionSelected(option: SnapshotRetentionOption) {
        _selectedOption.value = option
    }

    /**
     * Alterna la selección de un municipio dado su ID.
     * Si ya estaba seleccionado, se deselecciona; si no, se añade a la selección.
     *
     * @param id ID del municipio a alternar.
     */
    fun toggleMunicipio(id: String) {
        _selectedMunicipioIds.value = _selectedMunicipioIds.value.toMutableSet().also { set ->
            if (set.contains(id)) set.remove(id) else set.add(id)
        }
    }

    /**
     * Guarda la opción de retención actualmente seleccionada
     * en las preferencias para cada municipio seleccionado.
     */
    fun saveOptionToSelectedMunicipios() {
        val option = _selectedOption.value
        _selectedMunicipioIds.value.forEach { municipioId ->
            prefs.setRetentionForMunicipio(municipioId, option)
        }
    }

    /**
     * Forza la limpieza de snapshots antiguos que ya no cumplen con las políticas de retención.
     *
     * Esta operación se realiza en un dispatcher IO.
     */
    fun enforceCleanup() {
        viewModelScope.launch(Dispatchers.IO) {
            SnapshotCleanupManager(getApplication(), snapshotRepository).runCleanupIfNeeded()
        }
    }
}
