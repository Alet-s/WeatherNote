package com.alexser.weathernote.di

import com.alexser.weathernote.data.repository.WeatherRepositoryImpl
import com.alexser.weathernote.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module // ✅ REQUIRED
@InstallIn(SingletonComponent::class) // ✅ REQUIRED
object AppModule {

    @Provides
    @Singleton
    fun provideWeatherRepository(
        impl: WeatherRepositoryImpl
    ): WeatherRepository = impl
}
