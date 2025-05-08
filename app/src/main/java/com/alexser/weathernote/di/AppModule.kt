package com.alexser.weathernote.di

import com.alexser.weathernote.data.repository.MunicipioRepositoryImpl
import com.alexser.weathernote.data.repository.WeatherRepositoryImpl
import com.alexser.weathernote.domain.repository.MunicipioRepository
import com.alexser.weathernote.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWeatherRepository(
        impl: WeatherRepositoryImpl
    ): WeatherRepository = impl

    @Provides
    @Singleton
    fun provideMunicipioRepository(
        impl: MunicipioRepositoryImpl
    ): MunicipioRepository = impl
}
