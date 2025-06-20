package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.domain.model.getBottomNavItems

/**
 * Barra de navegación inferior que muestra los ítems de navegación definidos en la aplicación.
 *
 * @param currentRoute Ruta actualmente seleccionada para marcar el ítem activo.
 * @param onItemSelected Lambda que se ejecuta cuando se selecciona un ítem, recibiendo la ruta asociada.
 */
@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onItemSelected: (String) -> Unit
) {
    val navItems = getBottomNavItems()

    NavigationBar {
        navItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onItemSelected(item.route) },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(32.dp), // fixed icon box
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = item.icon,
                            contentDescription = item.label,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                },
                label = { Text(item.label) }
            )

        }
    }
}
