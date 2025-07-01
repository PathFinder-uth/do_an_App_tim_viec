package com.example.pathfinder.data.model

data class RegisterRequest(
    val email: String,
    val password: String,
    val confirmPassword: String
)