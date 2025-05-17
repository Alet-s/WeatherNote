package com.alexser.weathernote.domain.usecase

import com.alexser.weathernote.domain.model.BasicWeatherForecast
import com.alexser.weathernote.domain.repository.WeatherRepository
import javax.inject.Inject

class GetBasicWeatherForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(municipioId: String): Result<BasicWeatherForecast> {
        return try {
            val snapshot = repository.getSnapshot(municipioId)
            Result.success(snapshot)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
