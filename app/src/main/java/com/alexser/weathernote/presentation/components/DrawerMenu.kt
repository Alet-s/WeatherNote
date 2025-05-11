package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DrawerContent(onDestinationClicked: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("WeatherNote", fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
        DrawerItem("Inicio", Icons.Default.Home, onClick = { onDestinationClicked("home") })
        DrawerItem("Municipios", Icons.Default.CheckCircle, onClick = { onDestinationClicked("municipios") })
        DrawerItem("Municipios por hora", Icons.Default.MailOutline, onClick = { onDestinationClicked("municipios_horaria") })
        DrawerItem("Snapshots", Icons.Default.CheckCircle) {
            onDestinationClicked("snapshot_municipios_list")
        }

    }
}

@Composable
fun DrawerItem(label: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.width(16.dp))
        Text(label)
    }
}
