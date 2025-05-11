package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.domain.model.SnapshotReport

@Composable
fun SnapshotReportItem(snapshot: SnapshotReport) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("📅 ${snapshot.timestamp}")
            Text("🌡️ ${snapshot.temperature}°C | 💧 ${snapshot.humidity}%")
            Text("💨 ${snapshot.windSpeed} km/h | ☁️ ${snapshot.condition}")
        }
    }
}
