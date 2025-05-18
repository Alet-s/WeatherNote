package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.alexser.weathernote.domain.model.SavedMunicipio

@Composable
fun MunicipioDropdown(
    municipios: List<SavedMunicipio>,
    selected: SavedMunicipio?,
    onSelected: (SavedMunicipio) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            Text(selected?.nombre ?: "Selecciona un municipio")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            municipios.forEach {
                DropdownMenuItem(
                    text = { Text(it.nombre) },
                    onClick = {
                        onSelected(it)
                        expanded = false
                    }
                )
            }
        }
    }
}
