package com.alexser.weathernote.domain.usecase

import com.alexser.weathernote.domain.model.BasicWeatherForecast
import com.alexser.weathernote.domain.repository.WeatherRepository
import javax.inject.Inject

/**
 * Use case para obtener la predicción meteorológica básica de un municipio.
 *
 * Intenta obtener la predicción básica del repositorio y envuelve el resultado en un objeto [Result],
 * gestionando posibles excepciones.
 *
 * @property repository Repositorio para obtener datos meteorológicos.
 */
class GetBasicWeatherForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {

    /**
     * Obtiene la predicción meteorológica básica para el municipio indicado.
     *
     * @param municipioId ID del municipio del que se quiere obtener la predicción.
     * @return Un objeto [Result] que contiene un [BasicWeatherForecast] si la operación fue exitosa,
     * o una excepción en caso de fallo.
     */
    suspend operator fun invoke(municipioId: String): Result<BasicWeatherForecast> {
        return try {
            val snapshot = repository.getBasicWeatherForecast(municipioId)
            Result.success(snapshot)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
