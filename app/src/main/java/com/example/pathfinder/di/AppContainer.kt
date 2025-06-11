package com.example.pathfinder.di

import com.example.pathfinder.data.remote.FirebaseAuthServiceImpl
import com.example.pathfinder.data.repository.AuthRepository
import com.example.pathfinder.data.remote.IAuthService
import com.example.pathfinder.viewmodel.LoginViewModelFactory
import com.example.pathfinder.viewmodel.RegisterViewModelFactory
import com.example.pathfinder.viewmodel.EmailVerificationViewModelFactory

object AppContainer {
    val authService = FirebaseAuthServiceImpl()
    val authRepository = AuthRepository(authService)
    val loginViewModelFactory = LoginViewModelFactory(authRepository)
    val registerViewModelFactory = RegisterViewModelFactory(authRepository)
    val emailVerificationViewModelFactory = EmailVerificationViewModelFactory(authRepository)
}
