package com.alexser.weathernote.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexser.weathernote.data.firebase.AuthDataSource
import com.alexser.weathernote.domain.usecase.GetSnapshotUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val getSnapshotUseCase: GetSnapshotUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SnapshotUiState>(SnapshotUiState.Loading)
    val uiState: StateFlow<SnapshotUiState> = _uiState

    init {
        fetchSnapshot()
    }

    fun fetchSnapshot(municipioId: String = "10188") {
        viewModelScope.launch {
            _uiState.value = SnapshotUiState.Loading
            val result = getSnapshotUseCase(municipioId)
            result
                .onSuccess { data -> _uiState.value = SnapshotUiState.Success(data) }
                .onFailure { e -> _uiState.value = SnapshotUiState.Error(e.localizedMessage ?: "Unknown error") }
        }
    }

    fun logout() {
        authDataSource.signOut()
    }
}
