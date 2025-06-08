package com.alexser.weathernote.presentation.screens.snapshot

import com.alexser.weathernote.domain.model.SnapshotReport

/**
 * Estado UI para la pantalla que muestra los snapshots de un municipio específico.
 *
 * @property municipioId Identificador único del municipio seleccionado. Puede ser nulo si no hay municipio seleccionado.
 * @property municipioName Nombre del municipio seleccionado. Puede ser nulo si no hay municipio seleccionado.
 * @property snapshots Lista de informes meteorológicos (SnapshotReport) asociados al municipio.
 */
data class SnapshotMunicipioUiState(
    val municipioId: String? = null,
    val municipioName: String? = null,
    val snapshots: List<SnapshotReport> = emptyList()
)
