package com.alexser.weathernote.presentation.screens.home

import androidx.lifecycle.ViewModel
import com.alexser.weathernote.data.firebase.AuthDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val authDataSource: AuthDataSource
) : ViewModel() {

    fun logout() {
        authDataSource.signOut()
    }
}
