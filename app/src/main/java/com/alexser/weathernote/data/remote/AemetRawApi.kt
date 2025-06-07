package com.alexser.weathernote.data.remote

import ForecastDto
import com.alexser.weathernote.data.remote.model.HourlyForecastDto
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Interfaz que define las llamadas directas a URLs externas proporcionadas por la API de AEMET.
 *
 * Esta API se utiliza para obtener datos ya procesados en bruto desde enlaces dinámicos (campo `datos`)
 * proporcionados por las respuestas del endpoint principal de AEMET.
 */
interface AemetRawApi {

    /**
     * Obtiene la predicción meteorológica diaria a partir de una URL completa proporcionada por AEMET.
     *
     * @param forecastUrl URL completa al archivo JSON que contiene la predicción diaria.
     * @return Lista de [ForecastDto] con la información deserializada.
     */
    @GET
    suspend fun getDailyForecast(@Url forecastUrl: String): List<ForecastDto>

    /**
     * Obtiene la predicción meteorológica horaria a partir de una URL completa proporcionada por AEMET.
     *
     * @param url URL completa al archivo JSON que contiene la predicción horaria.
     * @return Lista de [HourlyForecastDto] con la información horaria deserializada.
     */
    @GET
    suspend fun getHourlyForecast(@Url url: String): List<HourlyForecastDto>

}
