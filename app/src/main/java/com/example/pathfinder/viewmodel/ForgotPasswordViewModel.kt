package com.example.pathfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pathfinder.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.pathfinder.viewmodel.state.ForgotPasswordState


class ForgotPasswordViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _state = MutableStateFlow<ForgotPasswordState>(ForgotPasswordState.Idle)
    val state: StateFlow<ForgotPasswordState> = _state

    fun sendResetEmail(email: String) {
        viewModelScope.launch {
            _state.value = ForgotPasswordState.Loading
            try {
                repository.sendResetEmail(email)
                _state.value = ForgotPasswordState.Success
            } catch (e: Exception) {
                _state.value = ForgotPasswordState.Error(e.message ?: "Đã xảy ra lỗi")
            }
        }
    }
}
