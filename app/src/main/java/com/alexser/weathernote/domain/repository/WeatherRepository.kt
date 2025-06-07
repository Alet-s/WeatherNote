package com.alexser.weathernote.domain.repository

import com.alexser.weathernote.data.remote.model.HourlyForecastDto
import com.alexser.weathernote.domain.model.BasicWeatherForecast
import com.alexser.weathernote.domain.model.DailyForecast

/**
 * Repositorio para acceder a los datos meteorológicos de un municipio.
 */
interface WeatherRepository {

    /**
     * Obtiene el pronóstico básico del tiempo para un municipio.
     *
     * @param municipioId Identificador del municipio.
     * @return Pronóstico básico que incluye temperaturas y condición actual.
     */
    suspend fun getBasicWeatherForecast(municipioId: String): BasicWeatherForecast

    /**
     * Obtiene el pronóstico horario para un municipio.
     *
     * @param municipioId Identificador del municipio.
     * @return Lista de datos del pronóstico por horas.
     */
    suspend fun getHourlyForecast(municipioId: String): List<HourlyForecastDto>

    /**
     * Obtiene el pronóstico diario para un municipio.
     *
     * @param municipioId Identificador del municipio.
     * @return Lista de pronósticos diarios.
     */
    suspend fun getDailyForecast(municipioId: String): List<DailyForecast>
}
