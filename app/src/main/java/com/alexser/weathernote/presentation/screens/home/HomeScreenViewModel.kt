package com.alexser.weathernote.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexser.weathernote.data.firebase.AuthDataSource
import com.alexser.weathernote.data.local.HomeMunicipioPreferences
import com.alexser.weathernote.domain.model.SavedMunicipio
import com.alexser.weathernote.domain.usecase.AddMunicipioUseCase
import com.alexser.weathernote.domain.usecase.FindMunicipioByNameUseCase
import com.alexser.weathernote.domain.usecase.GetHourlyForecastUseCase
import com.alexser.weathernote.domain.usecase.GetSnapshotUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.alexser.weathernote.data.remote.mapper.toHourlyForecastItems

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val getSnapshotUseCase: GetSnapshotUseCase,
    private val addMunicipioUseCase: AddMunicipioUseCase,
    private val findMunicipioByNameUseCase: FindMunicipioByNameUseCase,
    private val getHourlyForecastUseCase: GetHourlyForecastUseCase,
    private val homePrefs: HomeMunicipioPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow<SnapshotUiState>(SnapshotUiState.Idle)
    val uiState: StateFlow<SnapshotUiState> = _uiState

    private val _searchInput = MutableStateFlow("")
    val searchInput: StateFlow<String> = _searchInput

    init {
        // Observe changes in home municipio preference
        viewModelScope.launch {
            homePrefs.homeMunicipioId.collect { id ->
                if (!id.isNullOrBlank()) {
                    fetchSnapshot(id)
                } else {
                    _uiState.value = SnapshotUiState.Idle
                }
            }
        }
    }

    fun onSearchInputChanged(newInput: String) {
        _searchInput.value = newInput
    }

    fun searchAndFetchSnapshot() {
        viewModelScope.launch {
            _uiState.value = SnapshotUiState.Loading
            val id = findMunicipioByNameUseCase(_searchInput.value)
            if (id != null) {
                fetchSnapshot(id)
            } else {
                _uiState.value = SnapshotUiState.Error("Municipio not found")
            }
        }
    }

    fun fetchSnapshot(municipioId: String) {
        viewModelScope.launch {
            _uiState.value = SnapshotUiState.Loading
            val snapshotResult = getSnapshotUseCase(municipioId)

            if (snapshotResult.isSuccess) {
                val snapshot = snapshotResult.getOrThrow()
                val hourlyDto = getHourlyForecastUseCase(municipioId)
                val hourly = hourlyDto.firstOrNull()?.toHourlyForecastItems() ?: emptyList()

                _uiState.value = SnapshotUiState.Success(
                    data = snapshot,
                    hourly = hourly
                )
            } else {
                _uiState.value = SnapshotUiState.Error(
                    snapshotResult.exceptionOrNull()?.localizedMessage ?: "Failed to load weather data"
                )
            }
        }
    }



    fun addToFavorites() {
        val snapshot = (_uiState.value as? SnapshotUiState.Success)?.data ?: return
        val municipio = SavedMunicipio(id = snapshot.cityId, nombre = snapshot.city)
        viewModelScope.launch {
            addMunicipioUseCase(municipio)
            homePrefs.setHomeMunicipioId(municipio.id)
        }
    }

    fun clearHomeMunicipio() {
        viewModelScope.launch {
            homePrefs.clearHomeMunicipioId()
            _uiState.value = SnapshotUiState.Idle
        }
    }


    fun logout() {
        authDataSource.signOut()
    }
}
