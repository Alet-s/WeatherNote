package com.alexser.weathernote.presentation.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.presentation.components.BigWeatherCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    onLogout: () -> Unit,
    onAddFavorite: (String, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchInput by viewModel.searchInput.collectAsState()
    val searchFieldState = remember { mutableStateOf(TextFieldValue(searchInput)) }

    LaunchedEffect(searchInput) {
        if (searchFieldState.value.text != searchInput) {
            searchFieldState.value = TextFieldValue(searchInput)
        }
    }

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
            if (uiState is SnapshotUiState.Success) {
                val snapshot = (uiState as SnapshotUiState.Success).data
                FloatingActionButton(onClick = {
                    viewModel.addToFavorites()
                    onAddFavorite(snapshot.cityId, snapshot.city)
                }) {
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
            OutlinedTextField(
                value = searchFieldState.value,
                onValueChange = {
                    searchFieldState.value = it
                    viewModel.onSearchInputChanged(it.text)
                },
                label = { Text("Search municipio") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { viewModel.searchAndFetchSnapshot() },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Search")
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (uiState) {
                is SnapshotUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                is SnapshotUiState.Success -> {
                    Text(
                        "Today's Weather",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    val report = (uiState as SnapshotUiState.Success).data
                    BigWeatherCard(report = report)
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
