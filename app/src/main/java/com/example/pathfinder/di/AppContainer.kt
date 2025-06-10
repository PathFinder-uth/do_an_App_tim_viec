package com.example.pathfinder.di

import com.example.pathfinder.data.remote.FirebaseAuthServiceImpl
import com.example.pathfinder.data.remote.IAuthService
import com.example.pathfinder.data.repository.AuthRepository
import com.example.pathfinder.viewmodel.LoginViewModelFactory
import com.example.pathfinder.viewmodel.RegisterViewModelFactory


interface AppContainer {
    val authService: IAuthService
    val authRepository: AuthRepository
    val loginViewModelFactory: LoginViewModelFactory
    val registerViewModelFactory: RegisterViewModelFactory // Add this
}

class AppContainerImpl : AppContainer {
    override val authService: IAuthService by lazy {
        FirebaseAuthServiceImpl() // Or your actual API service
    }

    override val authRepository: AuthRepository by lazy {
        AuthRepository(authService)
    }

    override val loginViewModelFactory: LoginViewModelFactory by lazy {
        LoginViewModelFactory(authRepository)
    }

    override val registerViewModelFactory: RegisterViewModelFactory by lazy { // Initialize
        RegisterViewModelFactory(authRepository)
    }
}