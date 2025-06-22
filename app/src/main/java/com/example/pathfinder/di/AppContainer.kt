package com.example.pathfinder.di

import android.content.Context
import com.example.pathfinder.data.remote.FirebaseAuthServiceImpl
import com.example.pathfinder.data.remote.FirebaseUserServiceImpl
import com.example.pathfinder.data.remote.IAuthService
import com.example.pathfinder.data.remote.IFirebaseUserService
import com.example.pathfinder.data.repository.AuthRepository
import com.example.pathfinder.data.repository.ProfileRepository
import com.example.pathfinder.viewmodel.*
import com.example.pathfinder.data.local.SessionManager
object AppContainer {
    private lateinit var internalSessionManager: SessionManager
    val sessionManager: SessionManager
        get() = internalSessionManager
    // Firebase service
    val authService: IAuthService = FirebaseAuthServiceImpl()
    val userService: IFirebaseUserService = FirebaseUserServiceImpl()

    // Repositories
    val authRepository = AuthRepository(authService)
    val profileRepository = ProfileRepository(userService)
    lateinit var loginViewModelFactory: LoginViewModelFactory
        private set
    // ViewModel Factories

    val registerViewModelFactory = RegisterViewModelFactory(authRepository)
    val emailVerificationViewModelFactory = EmailVerificationViewModelFactory(authRepository)
    val forgotPasswordViewModelFactory = ForgotPasswordViewModelFactory(authRepository)
    val profileViewModelFactory = ProfileViewModelFactory(profileRepository)
    fun init(context: Context) {
        internalSessionManager = SessionManager(context)
        loginViewModelFactory = LoginViewModelFactory(authRepository, userService, sessionManager)
    }
}