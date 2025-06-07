package com.alexser.weathernote.domain.model

/**
 * Representa un municipio guardado por el usuario.
 *
 * @property id Identificador único del municipio.
 * @property nombre Nombre del municipio.
 */
data class SavedMunicipio(
    val id: String = "",
    val nombre: String = ""
)
