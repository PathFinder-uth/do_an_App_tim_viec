package com.example.pathfinder.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pathfinder.data.model.UserProfile
import com.example.pathfinder.viewmodel.state.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.pathfinder.data.repository.ProfileRepository

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun onFullNameChange(value: String) {
        _uiState.value = _uiState.value.copy(fullName = value)
    }

    fun onGenderChange(value: String) {
        _uiState.value = _uiState.value.copy(gender = value)
    }

    fun onAddressChange(value: String) {
        _uiState.value = _uiState.value.copy(address = value)
    }

    fun onPhoneChange(value: String) {
        _uiState.value = _uiState.value.copy(phone = value)
    }

    fun onBirthdayChange(value: String) {
        _uiState.value = _uiState.value.copy(birthday = value)
    }

    fun onContactChange(value: String) {
        _uiState.value = _uiState.value.copy(contact = value)
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            val result = repository.getUserProfile()
            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    fullName = it.fullName,
                    gender = it.gender,
                    address = it.address,
                    phone = it.phone,
                    birthday = it.birthday,
                    contact = it.contact,
                    avatarUrl = it.avatarUrl,
                    message = ""
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(message = it.message ?: "Đã xảy ra lỗi")
            }
        }
    }

    fun onSubmit() {
        viewModelScope.launch {
            val profile = UserProfile(
                fullName = _uiState.value.fullName,
                gender = _uiState.value.gender,
                address = _uiState.value.address,
                phone = _uiState.value.phone,
                birthday = _uiState.value.birthday,
                contact = _uiState.value.contact,
                avatarUrl = _uiState.value.avatarUrl
            )
            val result = repository.updateUserProfile(profile)
            result.onSuccess {
                _uiState.value = _uiState.value.copy(message = "Cập nhật thành công")
            }.onFailure {
                _uiState.value = _uiState.value.copy(message = it.message ?: "Đã xảy ra lỗi")
            }
        }
    }

    fun uploadAvatar(uri: Uri, context: Context) {
        viewModelScope.launch {
            val result = repository.uploadAvatar(uri,context)
            result.onSuccess { url ->
                _uiState.value = _uiState.value.copy(avatarUrl = url, message = "")
            }.onFailure {
                _uiState.value = _uiState.value.copy(message = it.message ?: "Không thể cập nhật ảnh")
            }
        }
    }

    fun setAvatarFromCropped(uri: Uri) {
        _uiState.value = _uiState.value.copy(tempAvatarUri = uri)
    }

    fun clearTempAvatar() {
        _uiState.value = _uiState.value.copy(tempAvatarUri = null)
    }
}