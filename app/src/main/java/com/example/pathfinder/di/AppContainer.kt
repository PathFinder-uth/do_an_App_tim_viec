package com.example.pathfinder.di

import com.example.pathfinder.data.remote.FirebaseAuthServiceImpl
import com.example.pathfinder.data.repository.AuthRepository

object AppContainer {
    val authService = FirebaseAuthServiceImpl()
    val authRepository = AuthRepository(authService)
}