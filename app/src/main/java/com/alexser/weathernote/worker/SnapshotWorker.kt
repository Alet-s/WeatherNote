package com.alexser.weathernote.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.alexser.weathernote.data.remote.mapper.toHourlyForecastFullItems
import com.alexser.weathernote.data.remote.mapper.toSnapshotReport
import com.alexser.weathernote.domain.model.SnapshotFrequency
import com.alexser.weathernote.domain.usecase.GetHourlyForecastUseCase
import com.alexser.weathernote.domain.usecase.GetSavedMunicipiosUseCase
import com.alexser.weathernote.domain.usecase.GetSnapshotFrequencyUseCase
import com.alexser.weathernote.domain.usecase.SaveSnapshotReportUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.LocalDateTime

@HiltWorker
class SnapshotWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val getSavedMunicipiosUseCase: GetSavedMunicipiosUseCase,
    private val getSnapshotFrequencyUseCase: GetSnapshotFrequencyUseCase,
    private val getHourlyForecastUseCase: GetHourlyForecastUseCase,
    private val saveSnapshotReportUseCase: SaveSnapshotReportUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val municipioList = getSavedMunicipiosUseCase().first()
            val nowHour = LocalDateTime.now().hour.toString().padStart(2, '0')
            val today = LocalDate.now().toString()

            municipioList.forEach { municipio ->
                val freq = getSnapshotFrequencyUseCase(municipio.id)
                if (freq == SnapshotFrequency.MANUAL) return@forEach

                val dtos = getHourlyForecastUseCase(municipio.id)
                val items = dtos.flatMap { it.toHourlyForecastFullItems() }
                val match = items.find { it.hour == nowHour } ?: return@forEach

                val snapshot = match.toSnapshotReport(
                    municipioId = municipio.id,
                    municipioName = municipio.nombre,
                    date = today
                )

                saveSnapshotReportUseCase(snapshot)
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("SnapshotWorker", "❌ Worker failed: ${e.message}")
            Result.retry()
        }
    }
}
