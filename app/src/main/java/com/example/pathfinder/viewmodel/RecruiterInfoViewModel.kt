package com.example.pathfinder.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pathfinder.data.model.RecruiterProfile
import com.example.pathfinder.data.repository.ProfileRepository
import com.example.pathfinder.di.AppContainer.sessionManager
import com.example.pathfinder.viewmodel.state.RecruiterUiState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RecruiterInfoViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecruiterUiState())
    val uiState: StateFlow<RecruiterUiState> = _uiState.asStateFlow()


    fun onCompanyNameChange(value: String) {
        _uiState.value = _uiState.value.copy(companyName = value, companyNameError = null)
    }

    fun onDescriptionChange(value: String) {
        _uiState.value = _uiState.value.copy(companyDescription = value, companyDescriptionError = null)
    }

    fun onAddressChange(value: String) {
        _uiState.value = _uiState.value.copy(companyAddress = value, companyAddressError = null)
    }

    fun onPhoneChange(value: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = value, phoneNumberError = null)
    }

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(email = value, emailError = null)
    }

    fun onWebsiteChange(value: String) {
        _uiState.value = _uiState.value.copy(website = value, websiteError = null)
    }

    fun onIndustryChange(value: String) {
        _uiState.value = _uiState.value.copy(industry = value, industryError = null)
    }

    fun onSizeChange(value: String) {
        _uiState.value = _uiState.value.copy(companySize = value, companySizeError = null)
    }

    fun resetSubmissionState() {
        _uiState.value = _uiState.value.copy(isSubmitted = false)
    }

    fun uploadLogo(uri: Uri, context: Context) {
        viewModelScope.launch {
            profileRepository.uploadAvatar(uri, context).onSuccess { url ->
                _uiState.value = _uiState.value.copy(logoUrl = url, logoUri = uri)
            }
        }
    }

    fun onSubmit() {
        val state = _uiState.value
        if (!validateForm()) return
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            // Báo lỗi ra UI hoặc logcat
            _uiState.value = _uiState.value.copy(isSubmitting = false)
            return
        }
        _uiState.value = state.copy(isSubmitting = true)

        val recruiterProfile = RecruiterProfile(
            uid = uid, // Sẽ được cập nhật trong service
            companyName = state.companyName,
            description = state.companyDescription,
            address = state.companyAddress,
            phone = state.phoneNumber,
            email = state.email,
            website = state.website,
            industry = state.industry,
            companySize = state.companySize,
            logoUrl = state.logoUrl,
            role = "recruiter"
        )

        viewModelScope.launch {
            profileRepository.updateRecruiterProfile(recruiterProfile).onSuccess {
                // 👇 Thêm dòng này
                sessionManager.saveSession(
                    loggedIn = true,
                    hasProfile = true,
                    role = "recruiter"
                )
                _uiState.value = _uiState.value.copy(isSubmitted = true)
            }
            _uiState.value = _uiState.value.copy(isSubmitting = false)
        }
    }

    private fun validateForm(): Boolean {
        val state = _uiState.value

        val companyNameError = if (state.companyName.isBlank()) "Không được bỏ trống" else null
        val companyDescriptionError = if (state.companyDescription.isBlank()) "Không được bỏ trống" else null
        val companyAddressError = if (state.companyAddress.isBlank()) "Không được bỏ trống" else null
        val phoneNumberError = if (state.phoneNumber.isBlank()) "Không được bỏ trống" else null
        val emailError = if (state.email.isBlank()) "Không được bỏ trống" else null
        val websiteError = if (state.website.isBlank()) "Không được bỏ trống" else null
        val industryError = if (state.industry.isBlank()) "Không được bỏ trống" else null
        val companySizeError = if (state.companySize.isBlank()) "Không được bỏ trống" else null

        _uiState.value = state.copy(
            companyNameError = companyNameError,
            companyDescriptionError = companyDescriptionError,
            companyAddressError = companyAddressError,
            phoneNumberError = phoneNumberError,
            emailError = emailError,
            websiteError = websiteError,
            industryError = industryError,
            companySizeError = companySizeError
        )

        return listOf(
            companyNameError,
            companyDescriptionError,
            companyAddressError,
            phoneNumberError,
            emailError,
            websiteError,
            industryError,
            companySizeError
        ).all { it == null }


    }
    fun loadProfile() {
        viewModelScope.launch {
            profileRepository.getRecruiterProfile().onSuccess { profile ->
                _uiState.value = _uiState.value.copy(
                    companyName = profile.companyName,
                    logoUrl = profile.logoUrl
                )
            }
        }
    }
}
