package com.alexser.weathernote.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.rounded.Tv
import androidx.compose.material.icons.rounded.Watch
import androidx.compose.material.icons.sharp.House
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.alexser.weathernote.R

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun getBottomNavItems(): List<BottomNavItem> {
    return listOf(
        BottomNavItem(stringResource(R.string.principal), "home", Icons.Sharp.House),
        BottomNavItem(stringResource(R.string.municipios), "municipios", Icons.Rounded.Place),
        BottomNavItem(stringResource(R.string.horaria), "municipios_horaria", Icons.Rounded.Watch),
        BottomNavItem(stringResource(R.string.snapshots), "snapshot_municipios_list", Icons.Rounded.PhotoCamera),
        BottomNavItem(stringResource(R.string.visor), "visor_municipios", Icons.Rounded.Tv)
    )
}
