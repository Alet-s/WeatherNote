package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun MetricsCheckboxes(selected: MutableMap<String, Boolean>) {
    Column {
        selected.forEach { (label, isChecked) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isChecked, onCheckedChange = { selected[label] = it })
                Text(label)
            }
        }
    }
}
