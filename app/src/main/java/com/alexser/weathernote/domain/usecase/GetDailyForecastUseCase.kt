package com.alexser.weathernote.domain.usecase

import com.alexser.weathernote.domain.model.DailyForecast
import com.alexser.weathernote.domain.repository.WeatherRepository
import javax.inject.Inject

/**
 * Use case para obtener la predicción meteorológica diaria de un municipio.
 *
 * Recupera una lista con la predicción diaria para el municipio indicado,
 * consultando el repositorio correspondiente.
 *
 * @property repository Repositorio para obtener datos meteorológicos.
 */
class GetDailyForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {

    /**
     * Obtiene la lista de predicciones diarias para un municipio.
     *
     * @param municipioId ID del municipio para el que se desea la predicción.
     * @return Lista de objetos [DailyForecast] con la predicción diaria.
     */
    suspend operator fun invoke(municipioId: String): List<DailyForecast> {
        return repository.getDailyForecast(municipioId)
    }
}
