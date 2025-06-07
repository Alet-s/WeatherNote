package com.alexser.weathernote.domain.usecase

import com.alexser.weathernote.domain.model.BasicWeatherForecast
import com.alexser.weathernote.domain.repository.WeatherRepository
import javax.inject.Inject

/**
 * Use case para obtener la previsión meteorológica básica de un municipio específico.
 *
 * @property repository Repositorio que proporciona los datos meteorológicos.
 */
class FetchBasicWeatherForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {

    /**
     * Obtiene la previsión meteorológica básica para el municipio dado.
     *
     * @param municipioId ID del municipio para el cual se quiere obtener la previsión.
     * @return Un objeto [BasicWeatherForecast] con los datos meteorológicos básicos.
     */
    suspend operator fun invoke(municipioId: String): BasicWeatherForecast {
        return repository.getBasicWeatherForecast(municipioId)
    }
}
