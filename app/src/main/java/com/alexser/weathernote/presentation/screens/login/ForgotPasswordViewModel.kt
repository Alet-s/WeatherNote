package com.alexser.weathernote.presentation.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexser.weathernote.data.firebase.AuthDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authDataSource: AuthDataSource
) : ViewModel() {

    var email by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var success by mutableStateOf(false)

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onSubmit() {
        viewModelScope.launch {
            isLoading = true
            error = null
            success = false

            val result = authDataSource.sendPasswordResetEmail(email.trim())
            isLoading = false

            result.fold(
                onSuccess = { success = true },
                onFailure = { error = it.localizedMessage ?: "Error" }
            )
        }
    }
}
