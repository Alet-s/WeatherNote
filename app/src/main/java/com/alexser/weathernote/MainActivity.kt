package com.alexser.weathernote

import WeathernoteTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alexser.weathernote.presentation.components.BottomNavigationBar
import com.alexser.weathernote.presentation.nav.AppNavHost
import com.alexser.weathernote.presentation.nav.AuthNavHost
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

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
                    val navController = rememberNavController()
                    val backStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = backStackEntry?.destination?.route

                    Scaffold(
                        topBar = {
                            TopAppBar(title = { Text("WeatherNote") })
                        },
                        bottomBar = {
                            BottomNavigationBar(
                                currentRoute = currentRoute,
                                onItemSelected = { route ->
                                    navController.navigate(route) {
                                        popUpTo(0) { inclusive = true }
                                        launchSingleTop = true
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
                                onLogout = { isAuthenticated = false }
                            )
                        }
                    }
                } else {
                    val navController = rememberNavController()
                    AuthNavHost(
                        navController = navController,
                        onAuthenticated = { isAuthenticated = true }
                    )
                }
            }
        }
    }
}
