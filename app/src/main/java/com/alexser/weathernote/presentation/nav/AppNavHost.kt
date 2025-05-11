package com.alexser.weathernote.presentation.nav

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
                onLogout = { onLogout() },
                onRequestAddFavorite = {
                    navController.navigate("municipios?fromHome=true")
                }
            )
        }

        composable(
            route = "municipios?fromHome={fromHome}",
            arguments = listOf(navArgument("fromHome") {
                defaultValue = "false"
            })
        ) { backStackEntry ->
            val fromHome = backStackEntry.arguments?.getString("fromHome") == "true"
            val viewModel = hiltViewModel<MunicipiosScreenViewModel>()
            MunicipiosScreen(
                viewModel = viewModel,
                showPrompt = fromHome
            )
        }



        composable("municipios_horaria") {
            val viewModel = hiltViewModel<MunicipiosHorariaScreenViewModel>()
            MunicipiosHorariaScreen(viewModel = viewModel)
        }
    }
}
