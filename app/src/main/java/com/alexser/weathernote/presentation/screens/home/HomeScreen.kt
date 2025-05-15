package com.alexser.weathernote.presentation.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.presentation.components.BigWeatherCard
import com.alexser.weathernote.presentation.components.HourlyForecastCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    onLogout: () -> Unit,
    onRequestAddFavorite: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("WeatherNote Home") },
                actions = {
                    TextButton(onClick = {
                        viewModel.logout()
                        onLogout()
                    }) {
                        Text("Logout")
                    }
                }
            )
        },
        floatingActionButton = {
            if (uiState is SnapshotUiState.Idle) {
                FloatingActionButton(onClick = onRequestAddFavorite) {
                    Icon(Icons.Default.Add, contentDescription = "Add Favorite")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            when (uiState) {
                is SnapshotUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                is SnapshotUiState.Success -> {
                    val report = (uiState as SnapshotUiState.Success).data
                    val hourly = (uiState as SnapshotUiState.Success).hourly

                    Text(
                        "Today's Weather",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    BigWeatherCard(
                        report = report,
                        onEditClick = onRequestAddFavorite,
                        onRemoveClick = viewModel::clearHomeMunicipio
                    )

                    if (hourly.isNotEmpty()) {
                        Text(
                            text = "Next hours",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                        )

                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(hourly) { item ->
                                HourlyForecastCard(item)
                            }
                        }
                    }

                    // ✅ Add manual snapshot generation button
                    Button(
                        onClick = { viewModel.generateSnapshotManually() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp)
                    ) {
                        Text("Generate Snapshot Now")
                    }
                }

                is SnapshotUiState.Error -> {
                    val message = (uiState as SnapshotUiState.Error).message
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error: $message",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 32.dp)
                        )
                    }
                }

                is SnapshotUiState.Idle -> {
                    Text(
                        text = "Search for a municipio to see its weather.",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 32.dp)
                    )
                }
            }
        }
    }
}
