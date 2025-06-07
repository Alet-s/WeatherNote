package com.alexser.weathernote.data.remote.mapper

import ForecastDto
import com.alexser.weathernote.domain.model.BasicWeatherForecast

/**
 * Convierte un objeto [ForecastDto], recuperado de la API de predicción diaria de AEMET,
 * en un modelo de dominio [BasicWeatherForecast] utilizado en la interfaz de usuario.
 *
 * Extrae únicamente el primer día de predicción disponible y lo transforma a un
 * resumen meteorológico básico: nombre del municipio, temperaturas máxima y mínima,
 * y una descripción del estado del cielo.
 *
 * Si no se encontraran datos válidos, se usarán valores por defecto como `"Desconocido"` o `0f`.
 *
 * @param municipioId El ID del municipio asociado al pronóstico.
 * @return instancia de [BasicWeatherForecast] con los datos principales de la predicción.
 */
fun ForecastDto.toBasicWeatherForecast(municipioId: String): BasicWeatherForecast {
    val dia = prediccion.dia.firstOrNull()
    val estado = dia?.estadoCielo?.firstOrNull { !it.descripcion.isNullOrBlank() }?.descripcion ?: "Desconocido"
    return BasicWeatherForecast(
        cityId = municipioId,
        city = nombre,
        date = dia?.fecha ?: "",
        maxTemp = dia?.temperatura?.maxima?.toFloat() ?: 0f,
        minTemp = dia?.temperatura?.minima?.toFloat() ?: 0f,
        condition = estado
    )
}
