package com.example.pathfinder.data.repository

import com.example.pathfinder.data.model.LoginRequest
import com.example.pathfinder.data.model.User
import com.example.pathfinder.data.remote.IAuthService

class AuthRepository(private val authService: IAuthService) {

    suspend fun loginWithEmail(loginRequest: LoginRequest): Result<User> {
        return authService.loginWithEmail(loginRequest)
    }
    suspend fun loginWithGoogle(token: String): Result<User> {
        return authService.loginWithGoogle(token)
    }
    fun getCurrentUser(): User? = authService.getCurrentUser()

    fun logout() = authService.logout()
}