package com.alexser.weathernote.presentation.screens.municipios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexser.weathernote.data.remote.model.HourlyForecastItem
import com.alexser.weathernote.domain.model.SavedMunicipio
import com.alexser.weathernote.domain.usecase.GetHourlyForecastUseCase
import com.alexser.weathernote.domain.usecase.GetSavedMunicipiosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import toHourlyForecastItems
import javax.inject.Inject

/**
 * ViewModel para la pantalla de predicción horaria de municipios.
 *
 * Gestiona la carga y almacenamiento en memoria de la lista de municipios guardados
 * y sus predicciones horarias correspondientes.
 *
 * @property getSavedMunicipiosUseCase Caso de uso para obtener la lista de municipios guardados.
 * @property getHourlyForecastUseCase Caso de uso para obtener la predicción horaria de un municipio.
 */
@HiltViewModel
class MunicipiosHorariaScreenViewModel @Inject constructor(
    private val getSavedMunicipiosUseCase: GetSavedMunicipiosUseCase,
    private val getHourlyForecastUseCase: GetHourlyForecastUseCase
) : ViewModel() {

    private val _hourlyForecasts =
        MutableStateFlow<Map<String, List<HourlyForecastItem>>>(emptyMap())
    val hourlyForecasts: StateFlow<Map<String, List<HourlyForecastItem>>> = _hourlyForecasts

    private val _municipios = MutableStateFlow<List<SavedMunicipio>>(emptyList())
    val municipios: StateFlow<List<SavedMunicipio>> = _municipios

    init {
        loadAllForecasts()
    }

    /**
     * Carga la lista de municipios guardados y, para cada municipio,
     * obtiene su predicción horaria, actualizando los estados correspondientes.
     *
     * Se usa collectLatest para actualizar municipios ante cambios en la fuente de datos.
     * Cada predicción se carga de forma concurrente para mejorar rendimiento.
     */
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

    /**
     * Función auxiliar para forzar la recarga de las predicciones horarias
     * y municipios desde las fuentes de datos.
     *
     * Se llama desde la pantalla para refrescar datos ante recomposiciones o eventos.
     */
    fun reloadForecasts() {
        loadAllForecasts()
    }

}
