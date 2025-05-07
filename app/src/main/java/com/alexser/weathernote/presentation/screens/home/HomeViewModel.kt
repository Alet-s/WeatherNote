package com.alexser.weathernote.presentation.screens.home

import androidx.lifecycle.ViewModel
import com.alexser.weathernote.data.firebase.AuthDataSource
import com.alexser.weathernote.domain.model.WeatherReport
import com.alexser.weathernote.domain.model.WeatherType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val authDataSource: AuthDataSource
) : ViewModel() {

    private val _weatherReport = MutableStateFlow(
        WeatherReport(
            city = "Madrid",
            date = LocalDate.now(),
            temperature = 28.5f,
            weatherType = WeatherType.SUNNY
        )
    )

    val weatherReport: StateFlow<WeatherReport> = _weatherReport

    fun logout() {
        authDataSource.signOut()
    }
}
