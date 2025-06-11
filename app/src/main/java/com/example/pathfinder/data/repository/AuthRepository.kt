package com.example.pathfinder.data.repository

import com.example.pathfinder.data.model.LoginRequest
import com.example.pathfinder.data.model.User
import com.example.pathfinder.data.remote.IAuthService
import com.example.pathfinder.data.model.RegisterRequest
class AuthRepository(private val authService: IAuthService) {

    suspend fun loginWithEmail(loginRequest: LoginRequest): Result<User> {
        return authService.loginWithEmail(loginRequest)
    }
    suspend fun loginWithGoogle(token: String): Result<User> {
        return authService.loginWithGoogle(token)
    }

    suspend fun register(request: RegisterRequest): Result<User> { // SỬA CHỮ KÝ HÀM NÀY
        return authService.register(request)
    }
    suspend fun sendEmailVerification() {
        authService.sendEmailVerification()
    }

    suspend fun isEmailVerified(): Boolean {
        return authService.isEmailVerified()
    }
    fun getCurrentUser(): User? = authService.getCurrentUser()

    fun logout() = authService.logout()
}