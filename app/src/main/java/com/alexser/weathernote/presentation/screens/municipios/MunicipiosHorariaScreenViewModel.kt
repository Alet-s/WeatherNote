package com.alexser.weathernote.presentation.screens.municipios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexser.weathernote.data.remote.mapper.toHourlyForecastItems
import com.alexser.weathernote.data.remote.model.HourlyForecastItem
import com.alexser.weathernote.domain.model.SavedMunicipio
import com.alexser.weathernote.domain.usecase.GetHourlyForecastUseCase
import com.alexser.weathernote.domain.usecase.GetSavedMunicipiosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MunicipiosHorariaScreenViewModel @Inject constructor(
    private val getSavedMunicipiosUseCase: GetSavedMunicipiosUseCase,
    private val getHourlyForecastUseCase: GetHourlyForecastUseCase
) : ViewModel() {

    private val _hourlyForecasts = MutableStateFlow<Map<String, List<HourlyForecastItem>>>(emptyMap())
    val hourlyForecasts: StateFlow<Map<String, List<HourlyForecastItem>>> = _hourlyForecasts

    private val _municipios = MutableStateFlow<List<SavedMunicipio>>(emptyList())
    val municipios: StateFlow<List<SavedMunicipio>> = _municipios

    init {
        loadAllForecasts()
    }

    private fun loadAllForecasts() {
        viewModelScope.launch {
            getSavedMunicipiosUseCase().collectLatest { list ->
                _municipios.value = list

                list.forEach { municipio ->
                    launch {
                        try {
                            val rawDtos = getHourlyForecastUseCase(municipio.id)
                            val items = rawDtos.flatMap { it.toHourlyForecastItems() }
                            _hourlyForecasts.update {
                                it + (municipio.id to items)
                            }
                        } catch (e: Exception) {
                            // Optional: Log error
                        }
                    }
                }
            }
        }

    }
    //Función auxiliar para exponer loadAllForecasts() en la screen correspondiente
    //Se utiliza para forzar la recarga de datos en cada recomposición
    fun reloadForecasts() {
        loadAllForecasts()
    }

}
