package com.alexser.weathernote.presentation.nav

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alexser.weathernote.presentation.screens.home.HomeScreen
import com.alexser.weathernote.presentation.screens.home.HomeScreenViewModel
import com.alexser.weathernote.presentation.screens.municipios.MunicipiosScreen
import com.alexser.weathernote.presentation.screens.municipios.MunicipiosScreenViewModel
import com.alexser.weathernote.presentation.screens.municipios.MunicipiosHorariaScreen
import com.alexser.weathernote.presentation.screens.municipios.MunicipiosHorariaScreenViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    onLogout: () -> Unit
) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            val viewModel = hiltViewModel<HomeScreenViewModel>()
            HomeScreen(
                viewModel = viewModel,
                onLogout = { onLogout() }
            )
        }

        composable("municipios") {
            val viewModel = hiltViewModel<MunicipiosScreenViewModel>()
            MunicipiosScreen(viewModel = viewModel)
        }

        composable("municipios_horaria") {
            val viewModel = hiltViewModel<MunicipiosHorariaScreenViewModel>()
            MunicipiosHorariaScreen(viewModel = viewModel)
        }
    }
}
