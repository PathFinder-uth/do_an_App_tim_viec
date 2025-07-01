package com.example.pathfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pathfinder.data.local.SessionManager
import com.example.pathfinder.data.remote.IFirebaseUserService
import com.example.pathfinder.viewmodel.state.SelectUserTypeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SelectUserTypeViewModel(
    private val sessionManager: SessionManager,
    private val firebaseUserService: IFirebaseUserService
) : ViewModel() {

    private val _uiState = MutableStateFlow<SelectUserTypeState>(SelectUserTypeState.Idle)
    val uiState: StateFlow<SelectUserTypeState> = _uiState

    fun selectRole(role: String) {
        viewModelScope.launch {
            try {
                val currentSession = sessionManager.getSession()
                val uid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid

                if (uid.isNullOrEmpty()) {
                    _uiState.value = SelectUserTypeState.Error("Không xác định được người dùng")
                    return@launch
                }

                // 🔥 Cập nhật role lên Firestore
                firebaseUserService.updateUserRole(uid, role)

                // Lưu local session
                sessionManager.saveSession(
                    loggedIn = currentSession.isLoggedIn,
                    hasProfile = false,
                    role = role,
                    uid = uid
                )

                _uiState.value = SelectUserTypeState.Success(role)
            } catch (e: Exception) {
                _uiState.value = SelectUserTypeState.Error(e.message ?: "Lỗi không xác định")
            }
        }
    }
}