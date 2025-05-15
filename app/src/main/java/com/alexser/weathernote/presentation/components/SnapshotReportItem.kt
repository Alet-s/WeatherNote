package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.domain.model.SnapshotReport

@Composable
fun SnapshotReportItem(
    snapshot: SnapshotReport,
    onDelete: (() -> Unit)? = null, // Optional deletion callback
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("📅 ${snapshot.timestamp}")
            Text("🌡️ ${snapshot.temperature ?: "-"}°C | 💧 ${snapshot.humidity ?: "-"}%")
            Text("💨 ${snapshot.windSpeed ?: "-"} km/h | ☁️ ${snapshot.condition ?: "-"}")

            if (onDelete != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.onError)
                }
            }
        }
    }
}
