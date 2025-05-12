package com.alexser.weathernote

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class WeatherNoteApp : Application() {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        // Custom WorkManager configuration
        val workManagerConfig = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

        try {
            WorkManager.initialize(this, workManagerConfig)
        } catch (e: IllegalStateException) {
            println("⚠️ WorkManager already initialized: ${e.message}")
        }
    }
}
