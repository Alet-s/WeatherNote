package com.alexser.weathernote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.alexser.weathernote.presentation.components.DrawerContent
import com.alexser.weathernote.presentation.nav.AppNavHost
import com.alexser.weathernote.presentation.nav.AuthNavHost
import com.alexser.weathernote.ui.theme.WeathernoteTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeathernoteTheme {
                var isAuthenticated by remember {
                    mutableStateOf(
                        FirebaseAuth.getInstance().currentUser?.isEmailVerified == true
                    )
                }

                if (isAuthenticated) {
                    LaunchedEffect(Unit) {
                        val workRequest = androidx.work.PeriodicWorkRequestBuilder<
                                com.alexser.weathernote.worker.SnapshotWorker
                                >(15, java.util.concurrent.TimeUnit.HOURS).build()

                        androidx.work.WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
                            "snapshot_worker",
                            androidx.work.ExistingPeriodicWorkPolicy.KEEP,
                            workRequest
                        )
                    }

                    // ✅ Authenticated flow
                    val navController = rememberNavController()
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()

                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            DrawerContent(
                                onDestinationClicked = { route ->
                                    scope.launch {
                                        drawerState.close()
                                        navController.navigate(route) {
                                            popUpTo(0) { inclusive = true } // Clear entire back stack
                                            launchSingleTop = true
                                        }
                                    }
                                }
                            )

                        }
                    ) {
                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    title = { Text("WeatherNote") },
                                    navigationIcon = {
                                        IconButton(onClick = {
                                            scope.launch { drawerState.open() }
                                        }) {
                                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                                        }
                                    }
                                )
                            }
                        ) { innerPadding ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                                color = MaterialTheme.colorScheme.background
                            ) {
                                AppNavHost(
                                    navController = navController,
                                    onLogout = {
                                        isAuthenticated = false
                                    }
                                )
                            }
                        }
                    }
                } else {
                    // ✅ Unauthenticated flow (login/signup/verify)
                    val navController = rememberNavController()

                    AuthNavHost(
                        navController = navController,
                        onAuthenticated = {
                            isAuthenticated = true
                        }
                    )
                }
            }
        }
    }
}
