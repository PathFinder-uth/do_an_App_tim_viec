package com.example.pathfinder.viewmodel.state

sealed class EmailVerificationState {
    object Idle : EmailVerificationState()
    object VerificationEmailSent : EmailVerificationState()
    data class Error(val message: String) : EmailVerificationState()
    object Verified : EmailVerificationState()
}
