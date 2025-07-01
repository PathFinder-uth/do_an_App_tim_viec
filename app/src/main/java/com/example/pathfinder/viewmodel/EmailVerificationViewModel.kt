package com.example.pathfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pathfinder.data.repository.AuthRepository
import com.example.pathfinder.viewmodel.state.EmailVerificationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class EmailVerificationViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow<EmailVerificationState>(EmailVerificationState.Idle)
    val state: StateFlow<EmailVerificationState> = _state

    fun sendVerificationEmail() {
        viewModelScope.launch {
            try {
                repository.sendEmailVerification()
                _state.value = EmailVerificationState.VerificationEmailSent
            } catch (e: Exception) {
                _state.value = EmailVerificationState.Error(e.message ?: "Lỗi không xác định")
            }
        }
    }

    fun checkEmailVerified() {
        viewModelScope.launch {
            val isVerified = repository.isEmailVerified()
            if (isVerified) {
                _state.value = EmailVerificationState.Verified
            }
        }
    }
}