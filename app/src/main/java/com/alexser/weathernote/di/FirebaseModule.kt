package com.alexser.weathernote.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de inyección de dependencias para Firebase.
 * Proporciona instancias singleton de FirebaseAuth y FirebaseFirestore.
 */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    /**
     * Proporciona la instancia singleton de FirebaseAuth para autenticación de usuarios.
     *
     * @return Instancia de FirebaseAuth.
     */
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Proporciona la instancia singleton de FirebaseFirestore para acceso a base de datos Firestore.
     *
     * @return Instancia de FirebaseFirestore.
     */
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}
