package com.alexser.weathernote.domain.usecase

import com.alexser.weathernote.data.remote.model.HourlyForecastDto
import com.alexser.weathernote.data.remote.model.HourlyForecastItem
import com.alexser.weathernote.domain.repository.WeatherRepository
import javax.inject.Inject

class GetHourlyForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(municipioId: String): List<HourlyForecastDto> {
        return repository.getHourlyForecast(municipioId)
    }
}
