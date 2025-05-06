package com.alexser.weathernote.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

//    @Provides
//    fun provideRetrofit(): Retrofit = Retrofit.Builder()
//        .baseUrl("https://opendata.aemet.es/api/")
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//
//    @Provides
//    fun provideAemetApi(retrofit: Retrofit): AemetApi =
//        retrofit.create(AemetApi::class.java)
}
