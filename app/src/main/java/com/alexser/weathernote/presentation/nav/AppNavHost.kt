package com.alexser.weathernote.presentation.nav

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alexser.weathernote.presentation.screens.home.HomeScreen
import com.alexser.weathernote.presentation.screens.home.HomeScreenViewModel
import com.alexser.weathernote.presentation.screens.login.LoginScreen
import com.alexser.weathernote.presentation.screens.login.LoginViewModel
import com.alexser.weathernote.presentation.screens.signUp.SignupScreen
import com.alexser.weathernote.presentation.screens.signUp.SignupViewModel

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            val viewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToSignup = {
                    navController.navigate("signup")
                }
            )
        }
        composable("signup") {
            val viewModel = hiltViewModel<SignupViewModel>()
            SignupScreen(
                viewModel = viewModel,
                onSignupSuccess = { navController.navigate("home") }
            )
        }
        composable("home") {
            val viewModel = hiltViewModel<HomeScreenViewModel>()
            HomeScreen(
                viewModel = viewModel,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

    }

    }



