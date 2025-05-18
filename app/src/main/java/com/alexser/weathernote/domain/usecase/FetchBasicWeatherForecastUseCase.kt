package com.alexser.weathernote.domain.usecase

import com.alexser.weathernote.domain.model.BasicWeatherForecast
import com.alexser.weathernote.domain.repository.WeatherRepository
import javax.inject.Inject

class FetchBasicWeatherForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(municipioId: String): BasicWeatherForecast {
        return repository.getBasicWeatherForecast(municipioId)
    }
}
