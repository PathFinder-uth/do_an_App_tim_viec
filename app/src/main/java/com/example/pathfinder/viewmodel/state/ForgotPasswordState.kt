package com.example.pathfinder.viewmodel.state

sealed class ForgotPasswordState {
    object Idle : ForgotPasswordState()
    object Success : ForgotPasswordState()
    data class Error(val message: String) : ForgotPasswordState()
    object Loading : ForgotPasswordState()
}