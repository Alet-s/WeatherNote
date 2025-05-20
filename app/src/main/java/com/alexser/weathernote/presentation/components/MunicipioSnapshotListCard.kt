package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.R
import com.alexser.weathernote.domain.model.SavedMunicipio

@Composable
fun MunicipioSnapshotListCard(
    municipio: SavedMunicipio,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = municipio.nombre, style = MaterialTheme.typography.titleMedium)
            Text(text = stringResource(R.string.toca_manejar_municipios), style = MaterialTheme.typography.bodyMedium)
        }
    }
}
