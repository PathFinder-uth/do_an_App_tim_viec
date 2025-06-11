package com.example.pathfinder.viewmodel.state

data class RegisterState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val agreeToTerms: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRegisteredSuccess: Boolean = false
)