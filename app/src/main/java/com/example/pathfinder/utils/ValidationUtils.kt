package com.example.pathfinder.utils

object ValidationUtils {
    fun isEmailValid(email: String?): Boolean {
        return !email.isNullOrBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String?): Boolean {
        return !password.isNullOrBlank() && password.length >= 6
    }
}