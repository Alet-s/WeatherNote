package com.alexser.weathernote.presentation.nav
//
//import androidx.compose.runtime.Composable
//import androidx.navigation.compose.rememberNavController
//import com.google.firebase.auth.FirebaseAuth
//
//@Composable
//fun AppEntryPoint() {
//    val navController = rememberNavController()
//    val user = FirebaseAuth.getInstance().currentUser
//
//    when {
//        user == null -> AuthNavHost(navController)
//        !user.isEmailVerified -> AuthNavHost(navController)
//        else -> AppNavHost(navController)
//    }
//}
