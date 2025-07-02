package com.example.pathfinder.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pathfinder.data.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.example.pathfinder.viewmodel.state.SettingsUiState

class SettingsViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadUserSettings()
    }

    // Tải cài đặt hiện tại của người dùng
    private fun loadUserSettings() {
        val userId = auth.currentUser?.uid ?: return
        _uiState.value = _uiState.value.copy(isLoading = true)

        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                val userProfile = document.toObject(UserProfile::class.java)
                if (userProfile != null) {
                    _uiState.value = SettingsUiState(
                        pushNotificationsEnabled = userProfile.pushNotificationsEnabled,
                        emailNotificationsEnabled = userProfile.emailNotificationsEnabled,
                        darkModePreference = userProfile.darkModePreference
                    )
                }
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
            .addOnFailureListener { e ->
                _uiState.value = _uiState.value.copy(isLoading = false, message = "Lỗi tải cài đặt")
                Log.e("SettingsVM", "Error loading settings", e)
            }
    }

    // Cập nhật một trường cài đặt cụ thể
    fun updateSetting(field: String, value: Any) {
        val userId = auth.currentUser?.uid ?: return

        db.collection("users").document(userId)
            .update(field, value)
            .addOnSuccessListener {
                // Cập nhật lại state trên UI
                when (field) {
                    "pushNotificationsEnabled" -> _uiState.value = _uiState.value.copy(pushNotificationsEnabled = value as Boolean)
                    "emailNotificationsEnabled" -> _uiState.value = _uiState.value.copy(emailNotificationsEnabled = value as Boolean)
                    "darkModePreference" -> _uiState.value = _uiState.value.copy(darkModePreference = value as String)
                }
                Log.d("SettingsVM", "$field updated to $value")
            }
            .addOnFailureListener { e ->
                _uiState.value = _uiState.value.copy(message = "Lỗi cập nhật cài đặt")
                Log.e("SettingsVM", "Error updating setting $field", e)
            }
    }
}
