package com.alexser.weathernote.data.remote.model

/**
 * DTO que representa la respuesta inicial de los endpoints de AEMET.
 * Contiene metadatos y una URL para acceder a los datos reales.
 *
 * @property descripcion Mensaje de estado o descripción del resultado.
 * @property estado Código de estado HTTP-like (ej. 200 para OK).
 * @property datos URL donde se encuentran los datos reales en formato JSON.
 */
data class MetadataResponseDto(
    val descripcion: String,
    val estado: Int,
    val datos: String
)
