package com.example.pathfinder.viewmodel.state

sealed class SelectUserTypeState {
    object Idle : SelectUserTypeState()
    object Loading : SelectUserTypeState()
    data class Success(val role: String) : SelectUserTypeState() // "candidate" hoặc "recruiter"
    data class Error(val message: String) : SelectUserTypeState()
}