package com.alexser.weathernote.data.remote

import com.alexser.weathernote.data.remote.model.MetadataResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interfaz Retrofit para acceder a los endpoints de predicción de AEMET.
 * Proporciona métodos para obtener metadatos de predicciones diarias y horarias.
 */
interface AemetApi {

    /**
     * Obtiene los metadatos de la predicción meteorológica diaria para un municipio específico.
     * La respuesta contiene una URL a los datos reales en formato JSON.
     *
     * @param municipioId ID INE del municipio.
     * @param apiKey Clave de API proporcionada por AEMET.
     * @return [MetadataResponseDto] con la URL de los datos diarios.
     */
    @GET("prediccion/especifica/municipio/diaria/{municipio}")
    suspend fun getForecastMetadata(
        @Path("municipio") municipioId: String,
        @Query("api_key") apiKey: String
    ): MetadataResponseDto

    /**
     * Obtiene los metadatos de la predicción meteorológica horaria para un municipio específico.
     * La respuesta contiene una URL a los datos reales en formato JSON.
     *
     * @param municipioId ID INE del municipio.
     * @param apiKey Clave de API proporcionada por AEMET.
     * @return [MetadataResponseDto] con la URL de los datos horarios.
     */
    @GET("prediccion/especifica/municipio/horaria/{municipio}")
    suspend fun getHourlyForecastMetadata(
        @Path("municipio") municipioId: String,
        @Query("api_key") apiKey: String
    ): MetadataResponseDto
}
