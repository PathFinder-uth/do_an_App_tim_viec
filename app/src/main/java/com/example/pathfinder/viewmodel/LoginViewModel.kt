package com.example.pathfinder.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pathfinder.data.local.SessionManager
import com.example.pathfinder.data.model.LoginRequest
import com.example.pathfinder.data.repository.AuthRepository
import com.example.pathfinder.viewmodel.state.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.pathfinder.data.remote.IFirebaseUserService
import com.example.pathfinder.data.model.UserProfile
import android.content.Context

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userService: IFirebaseUserService,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Email và mật khẩu không được để trống")
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _loginState.value = LoginState.Error("Email không đúng định dạng")
            return
        }

        _loginState.value = LoginState.Loading

        viewModelScope.launch {
            val result = authRepository.loginWithEmail(LoginRequest(email, password))
            result.onSuccess { user ->
                checkIfUserHasProfile(user.uid)
            }.onFailure {
                _loginState.value = LoginState.Error("Đăng nhập thất bại")
            }
        }

    }
    fun loginWithGoogle(token: String) {
        _loginState.value = LoginState.Loading

        viewModelScope.launch {
            val result = authRepository.loginWithGoogle(token)
            result.onSuccess { user ->
                checkIfUserHasProfile(user.uid)
            }.onFailure {
                _loginState.value = LoginState.Error(it.message ?: "Đăng nhập Google thất bại")
            }
        }
    }
    private suspend fun checkIfUserHasProfile(uid: String) {
        val profileResult = userService.getProfile()
        val hasProfile = profileResult.isSuccess
        _loginState.value = if (profileResult.isSuccess) {
            LoginState.Success(user = authRepository.getCurrentUser()!!, hasProfile = true)
        } else {
            LoginState.Success(user = authRepository.getCurrentUser()!!, hasProfile = false)
        }
        sessionManager.saveSession(loggedIn = true, hasProfile = hasProfile)
    }
    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}