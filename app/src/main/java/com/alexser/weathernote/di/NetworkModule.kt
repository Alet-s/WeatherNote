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

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // Gson instance
    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    // OkHttp client
    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    // Retrofit for main Aemet API (first request)
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

    // Retrofit for dynamic forecast URLs (second request)
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
