package com.alexser.weathernote.domain.usecase

import com.alexser.weathernote.data.remote.model.HourlyForecastDto
import com.alexser.weathernote.data.remote.model.HourlyForecastItem
import com.alexser.weathernote.domain.repository.WeatherRepository
import javax.inject.Inject

/**
 * Use case para obtener la predicción meteorológica por horas de un municipio.
 *
 * Consulta el repositorio para recuperar la lista completa de datos horarios
 * de predicción meteorológica para el municipio indicado.
 *
 * @property repository Repositorio para obtener datos meteorológicos.
 */
class GetHourlyForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {

    /**
     * Obtiene la lista de predicciones horarias para un municipio.
     *
     * @param municipioId ID del municipio para el que se desea la predicción horaria.
     * @return Lista de objetos [HourlyForecastDto] con la predicción por horas.
     */
    suspend operator fun invoke(municipioId: String): List<HourlyForecastDto> {
        return repository.getHourlyForecast(municipioId)
    }
}
