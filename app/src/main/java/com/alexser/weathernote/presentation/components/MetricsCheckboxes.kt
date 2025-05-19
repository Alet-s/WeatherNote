package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MetricsCheckboxes(selected: MutableMap<String, Boolean>) {
    val items = selected.entries.toList().chunked(2)

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items.forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                rowItems.forEach { (label, isChecked) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { selected[label] = it }
                        )
                        Text(label)
                    }
                }

                // If only 1 item in this row, add a spacer to fill second column space
                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
