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

/**
 * Módulo de inyección de dependencias para la aplicación WeatherNote.
 * Proporciona implementaciones singleton para repositorios, casos de uso y preferencias.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Proporciona la implementación del repositorio de datos meteorológicos.
     *
     * @param impl Implementación concreta de WeatherRepository.
     * @return WeatherRepository para inyección en la app.
     */
    @Provides
    @Singleton
    fun provideWeatherRepository(
        impl: WeatherRepositoryImpl
    ): WeatherRepository = impl

    /**
     * Proporciona la implementación del repositorio de municipios guardados.
     *
     * @param impl Implementación concreta de MunicipioRepository.
     * @return MunicipioRepository para inyección en la app.
     */
    @Provides
    @Singleton
    fun provideMunicipioRepository(
        impl: MunicipioRepositoryImpl
    ): MunicipioRepository = impl

    /**
     * Proporciona el caso de uso para obtener la predicción horaria.
     *
     * @param repository Repositorio meteorológico usado por el caso de uso.
     * @return Instancia de GetHourlyForecastUseCase.
     */
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

    /**
     * Proporciona la implementación del repositorio de snapshots meteorológicos,
     * encargado de guardar, obtener y eliminar snapshot reports en Firestore.
     *
     * @param firestore Instancia de FirebaseFirestore.
     * @param firebaseAuth Instancia de FirebaseAuth.
     * @return SnapshotReportRepository para inyección en la app.
     */
    @Provides
    @Singleton
    fun provideSnapshotReportRepository(
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ): SnapshotReportRepository {
        return SnapshotReportRepositoryImpl(firestore, firebaseAuth)
    }

    /**
     * Proporciona la instancia de SnapshotPreferences para el manejo de preferencias locales.
     *
     * @param application Contexto de la aplicación.
     * @return SnapshotPreferences para almacenamiento local de configuración.
     */
    @Provides
    @Singleton
    fun provideSnapshotPreferences(
        application: Application
    ): SnapshotPreferences {
        return SnapshotPreferences(application.applicationContext)
    }




}
