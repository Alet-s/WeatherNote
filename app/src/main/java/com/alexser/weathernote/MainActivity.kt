package com.alexser.weathernote

import WeathernoteTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

                var showSignOutDialog by remember { mutableStateOf(false) }

                if (isAuthenticated) {
                    val navController = rememberNavController()
                    val backStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = backStackEntry?.destination?.route

                    Scaffold(
                        topBar = {
                            TopAppBar(
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.background
                                ),
                                title = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.main_logo),
                                            contentDescription = "App Logo",
                                            modifier = Modifier
                                                .size(62.dp)
                                                .padding(end = 8.dp)
                                        )

                                        Text(
                                            text = "WeatherNote",
                                            fontFamily = FontFamily.Serif,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 24.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        Spacer(modifier = Modifier.width(12.dp))

                                        val email =
                                            FirebaseAuth.getInstance().currentUser?.email ?: ""
                                        if (email.isNotEmpty()) {
                                            Text(
                                                text = email,
                                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                                fontSize = 9.sp,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.weight(1f)
                                            )
                                        } else {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }

                                        IconButton(
                                            onClick = { showSignOutDialog = true },
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Logout,
                                                contentDescription = "Sign Out",
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            )
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

                        if (showSignOutDialog) {
                            AlertDialog(
                                onDismissRequest = { showSignOutDialog = false },
                                title = { Text("Cerrar sesión") },
                                text = { Text("¿Estás seguro de que deseas cerrar sesión?") },
                                confirmButton = {
                                    TextButton(onClick = {
                                        FirebaseAuth.getInstance().signOut()
                                        isAuthenticated = false
                                        showSignOutDialog = false
                                    }) {
                                        Text("Sí")
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = {
                                        showSignOutDialog = false
                                    }) {
                                        Text("Cancelar")
                                    }
                                }
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
