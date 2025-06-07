import com.alexser.weathernote.data.remote.model.HourlyForecastDto
import com.alexser.weathernote.data.remote.model.HourlyForecastFullItem
import com.alexser.weathernote.data.remote.model.HourlyForecastItem

/**
 * Convierte un [HourlyForecastDto] en una lista simplificada de [HourlyForecastItem].
 *
 * Esta función extrae únicamente la temperatura y el estado del cielo asociado a cada franja horaria,
 * omitiendo otras variables. Solo se incluyen las entradas con temperatura válida (entero parseable).
 *
 * @return Lista de [HourlyForecastItem] representando temperatura y condición por hora.
 */
fun HourlyForecastDto.toHourlyForecastItems(): List<HourlyForecastItem> {
    return prediccion.dia.flatMap { dia ->
        dia.temperatura.mapNotNull { tempEntry ->
            val condition = dia.estadoCielo.find { it.periodo == tempEntry.periodo }?.descripcion
                ?: "Desconocido"

            // Only map if temperature is a valid integer
            tempEntry.value.toIntOrNull()?.let { temperature ->
                HourlyForecastItem(
                    hour = tempEntry.periodo,
                    temperature = temperature,
                    condition = condition
                )
            }
        }
    }
}

/**
 * Convierte un [HourlyForecastDto] en una lista detallada de [HourlyForecastFullItem].
 *
 * Esta función extrae múltiples variables por hora: temperatura, sensación térmica,
 * estado del cielo, precipitación, nieve, humedad, viento y rachas máximas.
 * Se asocian los datos por franja horaria (`periodo`) y se incluyen solo
 * las entradas que contienen al menos temperatura válida.
 *
 * @return Lista de [HourlyForecastFullItem] con información meteorológica completa por hora.
 */
fun HourlyForecastDto.toHourlyForecastFullItems(): List<HourlyForecastFullItem> {
    return prediccion.dia.flatMap { dia ->
        val temps = dia.temperatura.associateBy { it.periodo }
        val feels = dia.sensTermica?.associateBy { it.periodo } ?: emptyMap()
        val skies = dia.estadoCielo.associateBy { it.periodo }
        val precs = dia.precipitacion?.associateBy { it.periodo } ?: emptyMap()
        val snows = dia.nieve?.associateBy { it.periodo } ?: emptyMap()
        val hums = dia.humedadRelativa?.associateBy { it.periodo } ?: emptyMap()
        val winds =
            dia.vientoAndRachaMax?.filter { it.direccion != null && it.direccion.isNotEmpty() }
                ?.associateBy { it.periodo } ?: emptyMap()
        val gusts = dia.vientoAndRachaMax?.filter { it.value != null }?.associateBy { it.periodo }
            ?: emptyMap()

        temps.mapNotNull { (hour, tempDto) ->
            HourlyForecastFullItem(
                hour = hour,
                temperature = tempDto.value.toIntOrNull(),
                feelsLike = feels[hour]?.value?.toIntOrNull(),
                condition = skies[hour]?.descripcion,
                precipitation = precs[hour]?.value?.toDoubleOrNull(),
                snow = snows[hour]?.value?.toDoubleOrNull(),
                humidity = hums[hour]?.value?.toIntOrNull(),
                windDirection = winds[hour]?.direccion?.firstOrNull(),
                windSpeed = winds[hour]?.velocidad?.firstOrNull()?.toIntOrNull(),
                maxGust = gusts[hour]?.value?.toIntOrNull()
            )
        }
    }
}

