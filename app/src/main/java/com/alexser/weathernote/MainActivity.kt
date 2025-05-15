package com.alexser.weathernote

import WeathernoteTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.alexser.weathernote.presentation.components.DrawerContent
import com.alexser.weathernote.presentation.nav.AppNavHost
import com.alexser.weathernote.presentation.nav.AuthNavHost
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
