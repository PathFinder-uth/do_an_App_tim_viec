package com.example.pathfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pathfinder.data.remote.IFirebaseUserService
import com.example.pathfinder.data.repository.AuthRepository
import com.example.pathfinder.data.local.SessionManager

class LoginViewModelFactory(
    private val authRepository: AuthRepository,
    private val userService: IFirebaseUserService,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(authRepository, userService, sessionManager) as T
    }
}