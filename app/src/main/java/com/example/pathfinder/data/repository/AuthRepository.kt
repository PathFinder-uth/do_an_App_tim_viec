package com.example.pathfinder.data.repository

import com.example.pathfinder.data.model.LoginRequest
import com.example.pathfinder.data.model.RegisterRequest
import com.example.pathfinder.data.model.User
import com.example.pathfinder.data.remote.IAuthService

class AuthRepository(private val authService: IAuthService) {

    suspend fun login(email: String, password: String): Result<User> {
        // Đảm bảo authService.login trả về Result<User>
        return authService.login(email, password)
    }

    suspend fun loginWithEmail(loginRequest: LoginRequest): Result<User> {
        return authService.loginWithEmail(loginRequest)
    }
    suspend fun loginWithGoogle(token: String): Result<User> {
        return authService.loginWithGoogle(token)
    }
    suspend fun loginWithFacebook(token: String): Result<User> {
        return authService.loginWithFacebook(token)
    }
    fun getCurrentUser(): User? {
        return authService.getCurrentUser()
    }

    fun logout() = authService.logout()

    suspend fun register(request: RegisterRequest): Result<User> { // SỬA CHỮ KÝ HÀM NÀY
        return authService.register(request)
    }
}