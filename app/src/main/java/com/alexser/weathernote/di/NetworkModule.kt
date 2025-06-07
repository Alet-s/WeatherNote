package com.alexser.weathernote.di

import com.alexser.weathernote.data.remote.AemetApi
import com.alexser.weathernote.data.remote.AemetRawApi
import com.alexser.weathernote.utils.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Módulo de inyección de dependencias para la red (Network).
 * Proporciona instancias singleton de Gson, OkHttpClient y Retrofit configurados para AEMET API.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Proporciona una instancia singleton de Gson para la serialización y deserialización JSON.
     *
     * @return Instancia de Gson.
     */
    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    /**
     * Proporciona una instancia singleton de OkHttpClient para gestionar las peticiones HTTP.
     *
     * @return Instancia de OkHttpClient.
     */
    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    /**
     * Proporciona una instancia singleton de Retrofit configurada para la API principal de AEMET.
     * Esta instancia se utiliza para realizar la primera petición a la API.
     *
     * @param okHttpClient Cliente HTTP para Retrofit.
     * @param gson Conversor Gson para Retrofit.
     * @return Instancia de AemetApi.
     */
    @Provides
    @Singleton
    fun provideAemetApi(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): AemetApi {
        return Retrofit.Builder()
            .baseUrl(Constants.AEMET_BASE_URL) // e.g., "https://opendata.aemet.es/opendata/api/"
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(AemetApi::class.java)
    }

    /**
     * Proporciona una instancia singleton de Retrofit configurada para consumir URLs dinámicas
     * que devuelven datos de predicción en bruto (segundas peticiones a URLs proporcionadas).
     * Utiliza una URL base dummy para permitir @Url dinámico en Retrofit.
     *
     * @param okHttpClient Cliente HTTP para Retrofit.
     * @param gson Conversor Gson para Retrofit.
     * @return Instancia de AemetRawApi.
     */
    @Provides
    @Singleton
    fun provideAemetRawApi(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): AemetRawApi {
        return Retrofit.Builder()
            .baseUrl("https://opendata.aemet.es/") // Dummy base for @Url support
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(AemetRawApi::class.java)
    }
}
