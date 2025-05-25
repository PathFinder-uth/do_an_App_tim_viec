package com.example.pathfinder.data.remote

import com.example.pathfinder.data.model.LoginRequest
import com.example.pathfinder.data.model.User

interface IAuthService {
    suspend fun loginWithEmail(loginRequest: LoginRequest): Result<User>
    fun getCurrentUser(): User?
    fun logout()
}