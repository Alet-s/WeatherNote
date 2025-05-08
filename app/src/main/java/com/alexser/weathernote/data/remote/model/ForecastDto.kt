data class ForecastDto(
    val nombre: String,
    val prediccion: PrediccionDto
)

data class PrediccionDto(
    val dia: List<DiaDto>
)

data class DiaDto(
    val fecha: String,
    val temperatura: TemperaturaDto,
    val estadoCielo: List<EstadoCieloDto>
)

data class TemperaturaDto(
    val maxima: Int,
    val minima: Int
)

data class EstadoCieloDto(
    val descripcion: String?
)

