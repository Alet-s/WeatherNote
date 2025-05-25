package com.alexser.weathernote.domain.usecase

import com.alexser.weathernote.domain.model.DailyForecast
import com.alexser.weathernote.domain.repository.WeatherRepository
import javax.inject.Inject

class GetDailyForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(municipioId: String): List<DailyForecast> {
        return repository.getDailyForecast(municipioId)
    }
}
