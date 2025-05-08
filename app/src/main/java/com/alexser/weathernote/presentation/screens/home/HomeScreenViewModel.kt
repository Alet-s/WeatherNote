package com.alexser.weathernote.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexser.weathernote.data.firebase.AuthDataSource
import com.alexser.weathernote.domain.model.Snapshot
import com.alexser.weathernote.domain.usecase.GetSnapshotUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val getSnapshotUseCase: GetSnapshotUseCase // ✅ injected use case
) : ViewModel() {

    private val _snapshot = MutableStateFlow<Snapshot?>(null)
    val snapshot: StateFlow<Snapshot?> = _snapshot

    init {
        fetchSnapshot()
    }

    private fun fetchSnapshot(municipioId: String = "10148") {
        viewModelScope.launch {
            val result = getSnapshotUseCase(municipioId)
            result.onSuccess { data ->
                _snapshot.value = data
            }.onFailure {
                // You can add an error flow or log here
            }
        }
    }

    fun logout() {
        authDataSource.signOut()
    }
}
