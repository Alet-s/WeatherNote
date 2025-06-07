package com.alexser.weathernote.presentation.nav

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alexser.weathernote.presentation.screens.login.ForgotPasswordScreen
import com.alexser.weathernote.presentation.screens.login.ForgotPasswordViewModel
import com.alexser.weathernote.presentation.screens.login.LoginScreen
import com.alexser.weathernote.presentation.screens.login.LoginViewModel
import com.alexser.weathernote.presentation.screens.signUp.SignupScreen
import com.alexser.weathernote.presentation.screens.signUp.SignupViewModel
import com.alexser.weathernote.presentation.screens.signUp.VerifyEmailScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AuthNavHost(
    navController: NavHostController,
    onAuthenticated: () -> Unit
) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {
            val viewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    onAuthenticated() // ✅ triggers recomposition in MainActivity
                },
                onNavigateToSignup = {
                    navController.navigate("signup")
                },
                onNavigateToForgotPassword = {
                    navController.navigate("forgot-password")
                }
            )
        }

        composable("signup") {
            val viewModel = hiltViewModel<SignupViewModel>()
            SignupScreen(
                viewModel = viewModel,
                onSignupSuccess = {
                    navController.navigate("verify-email")
                }
            )
        }

        composable("forgot-password") {
            val viewModel = hiltViewModel<ForgotPasswordViewModel>()
            ForgotPasswordScreen(
                viewModel = viewModel,
                onBackToLogin = {
                    navController.popBackStack() // or navController.navigate("login")
                }
            )
        }

        composable("verify-email") {
            VerifyEmailScreen(
                onContinue = {
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.reload()?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            if (user.isEmailVerified) {
                                onAuthenticated() // ✅ triggers graph switch
                            } else {
                                Toast.makeText(
                                    context,
                                    "Email not verified yet.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Could not refresh user info.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            )
        }
    }
}
