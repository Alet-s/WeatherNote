package com.alexser.weathernote.di

import android.app.Application
import com.alexser.weathernote.data.local.SnapshotPreferences
import com.alexser.weathernote.data.repository.MunicipioRepositoryImpl
import com.alexser.weathernote.data.repository.SnapshotConfigRepositoryImpl
import com.alexser.weathernote.data.repository.SnapshotReportRepositoryImpl
import com.alexser.weathernote.data.repository.WeatherRepositoryImpl
import com.alexser.weathernote.domain.repository.MunicipioRepository
import com.alexser.weathernote.domain.repository.SnapshotConfigRepository
import com.alexser.weathernote.domain.repository.SnapshotReportRepository
import com.alexser.weathernote.domain.repository.WeatherRepository
import com.alexser.weathernote.domain.usecase.GetHourlyForecastUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

    @Provides
    @Singleton
    fun provideGetHourlyForecastUseCase(
        repository: WeatherRepository
    ): GetHourlyForecastUseCase = GetHourlyForecastUseCase(repository)

    @Provides
    @Singleton
    fun provideSnapshotConfigRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): SnapshotConfigRepository {
        return SnapshotConfigRepositoryImpl(firestore, auth)
    }

    @Provides
    @Singleton
    fun provideSnapshotReportRepository(
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ): SnapshotReportRepository {
        return SnapshotReportRepositoryImpl(firestore, firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideSnapshotPreferences(
        application: Application
    ): SnapshotPreferences {
        return SnapshotPreferences(application.applicationContext)
    }




}
