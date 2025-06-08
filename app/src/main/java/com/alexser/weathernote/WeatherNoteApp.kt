package com.alexser.weathernote

import android.app.Application
import com.alexser.weathernote.domain.repository.SnapshotReportRepository
import com.alexser.weathernote.utils.SnapshotCleanupManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Clase Application principal de la app WeatherNote.
 *
 * Inicializa la inyección de dependencias con Hilt y ejecuta una tarea
 * en background para limpiar snapshots antiguos según las preferencias
 * definidas por el usuario.
 */
@HiltAndroidApp
class WeatherNoteApp : Application() {

    /**
     * Repositorio inyectado para acceder y manipular los SnapshotReports almacenados.
     */
    @Inject
    lateinit var snapshotReportRepository: SnapshotReportRepository

    /**
     * Métdo llamado al crear la aplicación.
     *
     * Aquí se lanza una corrutina en el contexto IO para ejecutar la limpieza
     * de snapshots antiguos mediante SnapshotCleanupManager.
     */
    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            SnapshotCleanupManager(this@WeatherNoteApp, snapshotReportRepository)
                .runCleanupIfNeeded()
        }
    }
}
