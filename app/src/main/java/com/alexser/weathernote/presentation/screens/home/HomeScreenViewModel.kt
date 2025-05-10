package com.alexser.weathernote.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexser.weathernote.data.firebase.AuthDataSource
import com.alexser.weathernote.domain.model.SavedMunicipio
import com.alexser.weathernote.domain.usecase.AddMunicipioUseCase
import com.alexser.weathernote.domain.usecase.GetSnapshotUseCase
import com.alexser.weathernote.domain.usecase.FindMunicipioByNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val getSnapshotUseCase: GetSnapshotUseCase,
    private val addMunicipioUseCase: AddMunicipioUseCase,
    private val findMunicipioByNameUseCase: FindMunicipioByNameUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SnapshotUiState>(SnapshotUiState.Idle)
    val uiState: StateFlow<SnapshotUiState> = _uiState

    private val _searchInput = MutableStateFlow("")
    val searchInput: StateFlow<String> = _searchInput

    fun onSearchInputChanged(newInput: String) {
        _searchInput.value = newInput
    }

    fun searchAndFetchSnapshot() {
        viewModelScope.launch {
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
            val result = getSnapshotUseCase(municipioId)
            result
                .onSuccess { data -> _uiState.value = SnapshotUiState.Success(data) }
                .onFailure { e -> _uiState.value = SnapshotUiState.Error(e.localizedMessage ?: "Unknown error") }
        }
    }

    fun addToFavorites() {
        val snapshot = (_uiState.value as? SnapshotUiState.Success)?.data ?: return
        val municipio = SavedMunicipio(id = snapshot.cityId, nombre = snapshot.city)
        viewModelScope.launch {
            addMunicipioUseCase(municipio)
        }
    }

    fun logout() {
        authDataSource.signOut()
    }
}
