package com.alexser.weathernote.data.remote.model

/**
 * Representa la respuesta general del endpoint de predicción horaria de AEMET.
 *
 * @property nombre Nombre del municipio.
 * @property prediccion Contenedor con los datos detallados de predicción por día.
 */
data class HourlyForecastDto(
    val nombre: String,
    val prediccion: HourlyPrediccionDto
)

/**
 * Contiene la lista de predicciones horarias para uno o más días.
 *
 * @property dia Lista de objetos que representan la predicción de cada día.
 */
data class HourlyPrediccionDto(
    val dia: List<HourlyDiaDto>
)

/**
 * Representa los datos meteorológicos horarios de un día concreto.
 *
 * @property fecha Fecha del día (formato ISO).
 * @property estadoCielo Lista con el estado del cielo por periodo horario.
 * @property temperatura Lista con temperaturas horarias.
 * @property sensTermica Lista con sensación térmica horaria (opcional).
 * @property precipitacion Lista con precipitación horaria en mm (opcional).
 * @property nieve Lista con nieve horaria en mm (opcional).
 * @property humedadRelativa Lista con humedad relativa horaria en % (opcional).
 * @property vientoAndRachaMax Lista con datos de viento y rachas máximas (opcional).
 */
data class HourlyDiaDto(
    val fecha: String,
    val estadoCielo: List<HourlyEstadoCieloDto>,
    val temperatura: List<HourlyTempDto>,
    val sensTermica: List<HourlyTempDto>?,
    val precipitacion: List<HourlyTempDto>?,
    val nieve: List<HourlyTempDto>?,
    val humedadRelativa: List<HourlyTempDto>?,
    val vientoAndRachaMax: List<HourlyWindDto>?
)

/**
 * Representa la información del viento y la racha máxima para un periodo horario.
 *
 * @property periodo Hora del día (por ejemplo, "14" para las 14:00).
 * @property direccion Lista con posibles direcciones del viento (puede variar).
 * @property velocidad Lista con velocidades del viento en km/h.
 * @property value Valor opcional representando la racha máxima en km/h.
 */
data class HourlyWindDto(
    val periodo: String,
    val direccion: List<String> = emptyList(),
    val velocidad: List<String> = emptyList(),
    val value: String? = null
)

/**
 * Describe el estado del cielo en un periodo horario concreto.
 *
 * @property periodo Hora del día (por ejemplo, "14" para las 14:00).
 * @property descripcion Texto descriptivo del estado del cielo (ej. "Despejado").
 */
data class HourlyEstadoCieloDto(
    val periodo: String,
    val descripcion: String?
)

/**
 * Representa un valor meteorológico horario genérico (temperatura, humedad, etc.).
 *
 * @property periodo Hora del día.
 * @property value Valor como string, que luego se convierte según corresponda.
 */
data class HourlyTempDto(
    val periodo: String,
    val value: String
)

/**
 * Modelo simplificado con solo la información esencial para mostrar en una tarjeta.
 *
 * @property hour Hora del día (ej. "14").
 * @property temperature Temperatura en grados Celsius.
 * @property condition Estado del cielo (ej. "Cubierto").
 */
data class HourlyForecastItem(
    val hour: String,        // "14" for 14:00
    val temperature: Int,    // 18
    val condition: String    // "Cubierto"
)

/**
 * Modelo completo con todos los datos meteorológicos disponibles para una hora específica.
 *
 * @property hour Hora del día.
 * @property temperature Temperatura en °C.
 * @property feelsLike Sensación térmica en °C.
 * @property condition Descripción del cielo (ej. "Nuboso").
 * @property precipitation Precipitación en mm.
 * @property snow Nieve en mm.
 * @property humidity Humedad relativa en %.
 * @property windDirection Dirección del viento (ej. "N").
 * @property windSpeed Velocidad del viento en km/h.
 * @property maxGust Racha máxima de viento en km/h.
 */
data class HourlyForecastFullItem(
    val hour: String,              // "14" for 14:00
    val temperature: Int?,         // °C
    val feelsLike: Int?,           // Sensación térmica
    val condition: String?,        // Estado del cielo (descripcion)
    val precipitation: Double?,    // mm de lluvia
    val snow: Double?,             // mm de nieve
    val humidity: Int?,            // %
    val windDirection: String?,   // Dirección del viento
    val windSpeed: Int?,          // Velocidad del viento (km/h)
    val maxGust: Int?              // Racha máxima (km/h)
)


