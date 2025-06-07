package com.alexser.weathernote.data.remote

import DiaDto
import com.alexser.weathernote.domain.model.BasicWeatherForecast
import com.alexser.weathernote.utils.Constants.AEMET_API_KEY
import com.alexser.weathernote.data.remote.mapper.toBasicWeatherForecast
import com.alexser.weathernote.data.remote.model.HourlyForecastDto
import javax.inject.Inject
import kotlin.collections.first

/**
 * Servicio de alto nivel que encapsula la lógica de obtención de datos meteorológicos
 * desde la API de AEMET.
 *
 * Esta clase actúa como intermediario entre los endpoints metadata y los datos reales
 * alojados en URLs externas proporcionadas por AEMET.
 */
class AemetService @Inject constructor(
    private val aemetApi: AemetApi,
    private val rawApi: AemetRawApi
) {

    /**
     * Obtiene una predicción meteorológica básica (máximas, mínimas, estado general) para un municipio dado.
     *
     * @param municipioId ID INE del municipio.
     * @return [BasicWeatherForecast] con los datos principales del día.
     */
    suspend fun getBasicWeatherForecast(municipioId: String): BasicWeatherForecast {
        val meta = aemetApi.getForecastMetadata(municipioId, AEMET_API_KEY)
        val fullData = rawApi.getDailyForecast(meta.datos)
        return fullData.first().toBasicWeatherForecast(municipioId)
    }

    /**
     * Obtiene la predicción horaria completa para un municipio.
     *
     * @param municipioId ID INE del municipio.
     * @return Lista de [HourlyForecastDto] con las predicciones horarias.
     */
    suspend fun getHourlyForecast(municipioId: String): List<HourlyForecastDto> {
        val meta = aemetApi.getHourlyForecastMetadata(municipioId, AEMET_API_KEY)
        return rawApi.getHourlyForecast(meta.datos)
    }

    /**
     * Obtiene la predicción diaria desglosada para un municipio.
     *
     * @param municipioId ID INE del municipio.
     * @return Lista de [DiaDto] con la predicción por días.
     */
    suspend fun getDailyForecast(municipioId: String): List<DiaDto> {
        val meta = aemetApi.getForecastMetadata(municipioId, AEMET_API_KEY)
        val forecastDto = rawApi.getDailyForecast(meta.datos)
        return forecastDto.first().prediccion.dia
    }


}
