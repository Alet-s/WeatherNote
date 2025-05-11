package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.domain.model.SnapshotFrequency

@Composable
fun SnapshotFrequencySelector(
    selectedFrequency: SnapshotFrequency,
    onFrequencySelected: (SnapshotFrequency) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SnapshotFrequency.values().forEach { frequency ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                RadioButton(
                    selected = selectedFrequency == frequency,
                    onClick = { onFrequencySelected(frequency) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = frequency.toDisplayName(), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
