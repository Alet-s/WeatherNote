package com.alexser.weathernote.data.firebase

sealed class AuthState {
    object Unauthenticated : AuthState()
    object EmailUnverified : AuthState()
    object Authenticated : AuthState()
}
