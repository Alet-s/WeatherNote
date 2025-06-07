/**
 * DTO principal: representa el pronóstico meteorológico de un municipio determinado.
 *
 * @property nombre Nombre del municipio.
 * @property prediccion Objeto que contiene la lista de predicciones por día.
 */
data class ForecastDto(
    val nombre: String,
    val prediccion: PrediccionDto
)

/**
 * Agrupa la predicción meteorológica por días.
 *
 * @property dia Lista de predicciones para días concretos.
 */
data class PrediccionDto(
    val dia: List<DiaDto>
)

/**
 * Representa la predicción meteorológica de un día específico.
 *
 * @property fecha Fecha de la predicción (formato ISO, ej. "2025-06-06").
 * @property temperatura Información de temperatura mínima y máxima.
 * @property estadoCielo Lista de condiciones del cielo, posiblemente por tramos horarios.
 */
data class DiaDto(
    val fecha: String,
    val temperatura: TemperaturaDto,
    val estadoCielo: List<EstadoCieloDto>
)

/**
 * Contiene las temperaturas máxima y mínima del día.
 *
 * @property maxima Temperatura máxima del día en grados Celsius.
 * @property minima Temperatura mínima del día en grados Celsius.
 */
data class TemperaturaDto(
    val maxima: Int,
    val minima: Int
)

/**
 * Describe el estado del cielo para un periodo específico del día.
 *
 * @property descripcion Descripción textual del estado del cielo (ej. "Despejado", "Nuboso").
 */
data class EstadoCieloDto(
    val descripcion: String?
)
