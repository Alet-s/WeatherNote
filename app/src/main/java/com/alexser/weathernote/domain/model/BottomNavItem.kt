package com.alexser.weathernote.domain.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.alexser.weathernote.R

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: Painter
)

@Composable
fun getBottomNavItems(): List<BottomNavItem> {
    return listOf(
        BottomNavItem(stringResource(R.string.principal), "home", painterResource(R.drawable.home)),
        BottomNavItem(stringResource(R.string.municipios), "municipios", painterResource(R.drawable.municipios)),
        BottomNavItem(stringResource(R.string.horaria), "municipios_horaria", painterResource(R.drawable.horaria)),
        BottomNavItem(stringResource(R.string.snapshots), "snapshot_municipios_list", painterResource(R.drawable.snapshots)),
        BottomNavItem(stringResource(R.string.visor), "visor_municipios", painterResource(R.drawable.visor))
    )

}
