package com.alexser.weathernote

import android.app.Application
import com.alexser.weathernote.domain.repository.SnapshotReportRepository
import com.alexser.weathernote.utils.SnapshotCleanupManager
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class WeatherNoteApp : Application() {

    @Inject
    lateinit var snapshotReportRepository: SnapshotReportRepository

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            SnapshotCleanupManager(this@WeatherNoteApp, snapshotReportRepository)
                .runCleanupIfNeeded()
        }
    }
}
