package com.alexser.weathernote.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexser.weathernote.data.remote.mapper.toHourlyForecastFullItems
import com.alexser.weathernote.data.remote.mapper.toSnapshotReport
import com.alexser.weathernote.domain.usecase.GetHourlyForecastUseCase
import com.alexser.weathernote.domain.usecase.SaveSnapshotReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SnapshotTestViewModel @Inject constructor(
    private val getHourlyForecastUseCase: GetHourlyForecastUseCase,
    private val saveSnapshotReportUseCase: SaveSnapshotReportUseCase
) : ViewModel() {

    private val _resultMessage = MutableStateFlow("")
    val resultMessage: StateFlow<String> = _resultMessage

    fun generateSnapshotForMadrid() {
        val municipioId = "28079"
        val municipioName = "Madrid"

        viewModelScope.launch {
            try {
                val forecastDtos = getHourlyForecastUseCase(municipioId)
                val fullItems = forecastDtos.flatMap { it.toHourlyForecastFullItems() }

                val currentHour = LocalDateTime.now().hour.toString().padStart(2, '0')
                val item = fullItems.find { it.hour.padStart(2, '0').startsWith(currentHour) }

                if (item == null) {
                    _resultMessage.value = "❌ No forecast found for current hour ($currentHour)"
                    return@launch
                }

                val snapshot = item.toSnapshotReport(
                    municipioId = municipioId,
                    municipioName = municipioName,
                    date = LocalDate.now().toString()
                )

                saveSnapshotReportUseCase(snapshot)
                _resultMessage.value = "✅ Snapshot saved successfully!"
            } catch (e: Exception) {
                _resultMessage.value = "❌ Error: ${e.message}"
            }
        }
    }
}
