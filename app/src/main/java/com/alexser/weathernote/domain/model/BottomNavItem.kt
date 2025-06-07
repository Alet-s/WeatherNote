package com.alexser.weathernote.domain.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.alexser.weathernote.R

/**
 * Representa un elemento de la barra de navegación inferior.
 *
 * @property label Texto que describe la pestaña o sección.
 * @property route Ruta o identificador de navegación asociado a este elemento.
 * @property icon Icono gráfico que se muestra en la barra de navegación.
 */
data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: Painter
)

/**
 * Función composable que devuelve la lista de elementos para la barra de navegación inferior.
 *
 * Cada elemento contiene un texto, una ruta para la navegación y un icono correspondiente.
 *
 * @return Lista de [BottomNavItem] que representa las secciones principales de la app.
 */
@Composable
fun getBottomNavItems(): List<BottomNavItem> {
    return listOf(
        BottomNavItem(stringResource(R.string.principal), "home", painterResource(R.drawable.home)),
        BottomNavItem(
            stringResource(R.string.municipios),
            "municipios",
            painterResource(R.drawable.municipios)
        ),
        BottomNavItem(
            stringResource(R.string.horaria),
            "municipios_horaria",
            painterResource(R.drawable.horaria)
        ),
        BottomNavItem(
            stringResource(R.string.snapshots),
            "snapshot_municipios_list",
            painterResource(R.drawable.snapshots)
        ),
        BottomNavItem(
            stringResource(R.string.visor),
            "visor_municipios",
            painterResource(R.drawable.visor)
        )
    )

}
