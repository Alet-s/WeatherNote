package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun SnapshotRangeSelector(
    totalCount: Int,
    selectedRange: ClosedFloatingPointRange<Float>,
    onRangeChange: (ClosedFloatingPointRange<Float>) -> Unit,
    getDateLabel: (Int) -> String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(getDateLabel(selectedRange.start.toInt()))
            Text(getDateLabel(selectedRange.endInclusive.toInt()))
        }

        Spacer(modifier = Modifier.height(8.dp))

        RangeSlider(
            value = selectedRange,
            onValueChange = onRangeChange,
            valueRange = 0f..(totalCount - 1).toFloat(),
            steps = if (totalCount > 2) totalCount - 2 else 0,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
