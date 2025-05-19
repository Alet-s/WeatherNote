package com.alexser.weathernote.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.rounded.Tv
import androidx.compose.material.icons.rounded.Watch
import androidx.compose.material.icons.sharp.House
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem("Home", "home", Icons.Sharp.House),
    BottomNavItem("Municipios", "municipios", Icons.Rounded.Place),
    BottomNavItem("Por hora", "municipios_horaria", Icons.Rounded.Watch),
    BottomNavItem("Snapshots", "snapshot_municipios_list", Icons.Rounded.PhotoCamera),
    BottomNavItem("Visor","visor_municipios", Icons.Rounded.Tv)
)
