package com.alexser.weathernote.data.remote

import com.alexser.weathernote.data.remote.model.MetadataResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AemetApi {
    @GET("prediccion/especifica/municipio/diaria/{municipio}")
    suspend fun getForecastMetadata(
        @Path("municipio") municipioId: String,
        @Query("api_key") apiKey: String
    ): MetadataResponseDto
}
