package com.example.pathfinder.di

import com.example.pathfinder.data.remote.FirebaseAuthServiceImpl
import com.example.pathfinder.data.remote.FirebaseUserServiceImpl
import com.example.pathfinder.data.remote.IAuthService
import com.example.pathfinder.data.remote.IFirebaseUserService
import com.example.pathfinder.data.repository.AuthRepository
import com.example.pathfinder.data.repository.ProfileRepository
import com.example.pathfinder.viewmodel.*

object AppContainer {

    // Firebase service
    val authService: IAuthService = FirebaseAuthServiceImpl()
    val userService: IFirebaseUserService = FirebaseUserServiceImpl()

    // Repositories
    val authRepository = AuthRepository(authService)
    val profileRepository = ProfileRepository(userService)

    // ViewModel Factories
    val loginViewModelFactory = LoginViewModelFactory(authRepository)
    val registerViewModelFactory = RegisterViewModelFactory(authRepository)
    val emailVerificationViewModelFactory = EmailVerificationViewModelFactory(authRepository)
    val forgotPasswordViewModelFactory = ForgotPasswordViewModelFactory(authRepository)
    val profileViewModelFactory = ProfileViewModelFactory(profileRepository)
}