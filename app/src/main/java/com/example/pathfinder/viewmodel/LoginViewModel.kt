package com.example.pathfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pathfinder.data.model.LoginRequest
import com.example.pathfinder.data.repository.AuthRepository
import com.example.pathfinder.viewmodel.state.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch



class LoginViewModel (private val authRepository: AuthRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Email và mật khẩu không được để trống")
            return
        }

        _loginState.value = LoginState.Loading

        viewModelScope.launch {
            val result = authRepository.loginWithEmail(LoginRequest(email, password))
            result.onSuccess { user ->
                _loginState.value = LoginState.Success(user)
            }.onFailure { exception ->
                _loginState.value = LoginState.Error(exception.message ?: "Đăng nhập thất bại")
            }
        }
    }
    fun loginWithGoogle(token: String) {
        _loginState.value = LoginState.Loading

        viewModelScope.launch {
            val result = authRepository.loginWithGoogle(token)
            result.onSuccess { user ->
                _loginState.value = LoginState.Success(user)
            }.onFailure { exception ->
                _loginState.value = LoginState.Error(exception.message ?: "Đăng nhập Google thất bại")
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}