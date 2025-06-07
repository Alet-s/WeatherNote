package com.alexser.weathernote.domain.model

/**
 * Representa un informe meteorológico (snapshot) asociado a un municipio,
 * con datos relevantes de la predicción y observación del clima en un momento dado.
 *
 * @property reportId Identificador único del informe
 * @property timestamp Fecha y hora en formato ISO o similar del momento del informe
 * @property municipioId Identificador del municipio al que corresponde el informe
 * @property municipioName Nombre del municipio asociado al informe
 * @property temperature Temperatura actual en grados Celsius (opcional)
 * @property feelsLike Temperatura sensación térmica en grados Celsius (opcional)
 * @property condition Estado o condición del cielo (e.g., soleado, nublado) (opcional)
 * @property precipitation Cantidad de precipitación en milímetros (opcional)
 * @property snow Cantidad de nieve en milímetros (opcional)
 * @property humidity Humedad relativa en porcentaje (opcional)
 * @property windDirection Dirección del viento (opcional)
 * @property windSpeed Velocidad del viento en km/h (opcional)
 * @property maxGust Máxima racha de viento en km/h (opcional)
 * @property userNote Nota o comentario adicional del usuario (opcional)
 */
data class SnapshotReport(
    val reportId: String = "",
    val timestamp: String = "",
    val municipioId: String = "",
    val municipioName: String = "",
    val temperature: Int? = null,
    val feelsLike: Int? = null,
    val condition: String? = null,
    val precipitation: Double? = null,
    val snow: Double? = null,
    val humidity: Int? = null,
    val windDirection: String? = null,
    val windSpeed: Int? = null,
    val maxGust: Int? = null,
    val userNote: String? = null
)
