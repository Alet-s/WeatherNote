package com.alexser.weathernote.presentation.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.alexser.weathernote.domain.model.SavedMunicipio
import com.alexser.weathernote.presentation.screens.home.HomeScreen
import com.alexser.weathernote.presentation.screens.home.HomeScreenViewModel
import com.alexser.weathernote.presentation.screens.municipios.MunicipiosHorariaScreen
import com.alexser.weathernote.presentation.screens.municipios.MunicipiosHorariaScreenViewModel
import com.alexser.weathernote.presentation.screens.municipios.MunicipiosScreen
import com.alexser.weathernote.presentation.screens.municipios.MunicipiosScreenViewModel
import com.alexser.weathernote.presentation.screens.snapshot.SnapshotConfigScreen
import com.alexser.weathernote.presentation.screens.snapshot.SnapshotMunicipioScreen
import com.alexser.weathernote.presentation.screens.snapshot.SnapshotMunicipiosListScreen
import com.alexser.weathernote.presentation.screens.visor.VisorScreen
import com.alexser.weathernote.presentation.screens.visor.VisorMunicipioListScreen

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

        composable("snapshot_municipios_list") {
            SnapshotMunicipiosListScreen(navController = navController)
        }

        composable(
            route = "snapshotMunicipio/{municipioId}/{municipioName}",
            arguments = listOf(
                navArgument("municipioId") { defaultValue = "" },
                navArgument("municipioName") { defaultValue = "" }
            )
        ) { backStackEntry ->
            val municipioId = backStackEntry.arguments?.getString("municipioId") ?: return@composable
            val municipioName = backStackEntry.arguments?.getString("municipioName") ?: "Desconocido"

            SnapshotMunicipioScreen(
                municipio = SavedMunicipio(
                    id = municipioId,
                    nombre = municipioName
                ),
                navController = navController
            )
        }

        composable("snapshotConfig") {
            SnapshotConfigScreen(navController = navController)
        }

        // ✅ New: Visor Municipio List (Main entry to visor flow)
        composable("visor_municipios") {
            VisorMunicipioListScreen(navController = navController)
        }

        // ✅ New: VisorScreen proper, receives municipioId and municipioName
        composable(
            route = "visor/{municipioId}/{municipioNombre}",
            arguments = listOf(
                navArgument("municipioId") { defaultValue = "" },
                navArgument("municipioNombre") { defaultValue = "Desconocido" }
            )
        ) { backStackEntry ->
            val municipioId = backStackEntry.arguments?.getString("municipioId") ?: return@composable
            val municipioNombre = backStackEntry.arguments?.getString("municipioNombre") ?: "Desconocido"

            VisorScreen(
                municipio = SavedMunicipio(id = municipioId, nombre = municipioNombre)
            )
        }
    }
}
