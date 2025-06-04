package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.domain.model.SnapshotReport

@Composable
fun SnapshotReportItem(
    snapshot: SnapshotReport,
    onDelete: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    showCheckbox: Boolean = false,
    checked: Boolean = false,
    onCheckToggle: (() -> Unit)? = null,
    onNoteClick: (() -> Unit)? = null
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

                // Orange dot shown visibly
                if (!snapshot.userNote.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .offset(x = 6.dp, y = 0.dp), // Push into view beyond padding
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFFF9800) // Orange
                        ) {
                            Box(modifier = Modifier.size(8.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(snapshot.timestamp)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Thermostat, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("${snapshot.temperature ?: "-"}°C")

                    Spacer(modifier = Modifier.width(16.dp))

                    Icon(Icons.Default.WaterDrop, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("${snapshot.humidity ?: "-"}%")
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Air, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("${snapshot.windSpeed ?: "-"} km/h")

                    Spacer(modifier = Modifier.width(16.dp))

                    Icon(Icons.Default.Cloud, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(snapshot.condition ?: "-")
                }
            }

            // Note icon button
            onNoteClick?.let {
                IconButton(
                    onClick = it,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp) 
                ) {
                    Icon(
                        imageVector = Icons.Outlined.EditNote,
                        contentDescription = "Add/Edit Note"
                    )
                }
            }

            // Checkbox if enabled
            if (showCheckbox && onCheckToggle != null) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = { onCheckToggle() },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                )
            }

            // Delete icon
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
