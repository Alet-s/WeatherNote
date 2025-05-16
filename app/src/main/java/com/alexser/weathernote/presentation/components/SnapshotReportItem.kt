package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.domain.model.SnapshotReport
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete

@Composable
fun SnapshotReportItem(
    snapshot: SnapshotReport,
    onDelete: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    showCheckbox: Boolean = false,
    checked: Boolean = false,
    onCheckToggle: (() -> Unit)? = null
) {
    val cardColor = if (checked) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    val borderStroke = if (checked) {
        BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    } else null

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        border = borderStroke
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("📅 ${snapshot.timestamp}")
                Text("🌡️ ${snapshot.temperature ?: "-"}°C | 💧 ${snapshot.humidity ?: "-"}%")
                Text("💨 ${snapshot.windSpeed ?: "-"} km/h | ☁️ ${snapshot.condition ?: "-"}")
            }

            if (showCheckbox && onCheckToggle != null) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = { onCheckToggle() },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                )
            }

            if (onDelete != null) {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete snapshot",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
