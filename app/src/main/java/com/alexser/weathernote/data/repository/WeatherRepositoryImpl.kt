package com.alexser.weathernote.data.repository

import com.alexser.weathernote.data.remote.AemetService
import com.alexser.weathernote.data.remote.mapper.toDailyForecast
import com.alexser.weathernote.data.remote.model.HourlyForecastDto
import com.alexser.weathernote.domain.model.BasicWeatherForecast
import com.alexser.weathernote.domain.model.DailyForecast
import com.alexser.weathernote.domain.repository.WeatherRepository
import javax.inject.Inject

/**
 * Implementación de [WeatherRepository] que obtiene datos meteorológicos
 * utilizando el servicio externo [AemetService].
 *
 * @property aemetService Servicio para acceder a la API de AEMET.
 */
class WeatherRepositoryImpl @Inject constructor(
    private val aemetService: AemetService
) : WeatherRepository {

    /**
     * Obtiene la previsión básica diaria para un municipio dado.
     *
     * @param municipioId ID del municipio para el que se obtiene la previsión.
     * @return Objeto [BasicWeatherForecast] con datos resumidos del tiempo.
     */
    override suspend fun getBasicWeatherForecast(municipioId: String): BasicWeatherForecast {
        return aemetService.getBasicWeatherForecast(municipioId)
    }

    /**
     * Obtiene la previsión meteorológica horaria para un municipio.
     *
     * @param municipioId ID del municipio para el que se obtiene la previsión horaria.
     * @return Lista de [HourlyForecastDto] con la previsión por horas.
     */
    override suspend fun getHourlyForecast(municipioId: String): List<HourlyForecastDto> {
        return aemetService.getHourlyForecast(municipioId)
    }

    /**
     * Obtiene la previsión diaria detallada para un municipio.
     *
     * @param municipioId ID del municipio para el que se obtiene la previsión diaria.
     * @return Lista de [DailyForecast] con la previsión diaria.
     */
    override suspend fun getDailyForecast(municipioId: String): List<DailyForecast> {
        return aemetService.getDailyForecast(municipioId).map { it.toDailyForecast() }
    }


}
