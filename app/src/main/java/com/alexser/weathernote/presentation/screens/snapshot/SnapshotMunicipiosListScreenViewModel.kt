//package com.alexser.weathernote.presentation.screens.snapshot
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.alexser.weathernote.domain.model.SavedMunicipio
//import com.alexser.weathernote.domain.usecase.GetSavedMunicipiosUseCase
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class SnapshotMunicipiosListScreenViewModel @Inject constructor(
//    private val getSavedMunicipiosUseCase: GetSavedMunicipiosUseCase
//) : ViewModel() {
//
//    private val _municipios = MutableStateFlow<List<SavedMunicipio>>(emptyList())
//    val municipios: StateFlow<List<SavedMunicipio>> = _municipios
//
//    init {
//        viewModelScope.launch {
//            getSavedMunicipiosUseCase().collect { savedList ->
//                _municipios.value = savedList
//            }
//        }
//    }
//}
