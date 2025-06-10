package com.example.pathfinder.viewmodel

import android.util.Patterns // Import Patterns để kiểm tra email
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pathfinder.data.model.RegisterRequest
import com.example.pathfinder.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update // Quan trọng: import hàm update
import kotlinx.coroutines.launch

// Data class để giữ trạng thái UI cho màn hình Đăng ký
data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val agreeToTerms: Boolean = false,
    val isLoading: Boolean = false,
    val isRegisteredSuccess: Boolean = false,
    val errorMessage: String? = null
)

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    // MutableStateFlow cho phép cập nhật trạng thái từ ViewModel
    private val _uiState = MutableStateFlow(RegisterUiState())
    // StateFlow công khai cho UI để quan sát (read-only)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    // Hàm cập nhật Email khi người dùng nhập
    fun onEmailChange(email: String) {
        _uiState.update { currentState ->
            currentState.copy(email = email, errorMessage = null) // Cập nhật email và xóa lỗi
        }
    }

    // Hàm cập nhật Password khi người dùng nhập
    fun onPasswordChange(password: String) {
        _uiState.update { currentState ->
            currentState.copy(password = password, errorMessage = null) // Cập nhật password và xóa lỗi
        }
    }

    // Hàm cập nhật Confirm Password khi người dùng nhập
    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update { currentState ->
            currentState.copy(confirmPassword = confirmPassword, errorMessage = null) // Cập nhật confirmPassword và xóa lỗi
        }
    }

    // Hàm cập nhật trạng thái đồng ý điều khoản
    fun onAgreeToTermsChange(agreed: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(agreeToTerms = agreed, errorMessage = null) // Cập nhật agreeToTerms và xóa lỗi
        }
    }

    // Hàm xử lý logic đăng ký
    fun register() {
        viewModelScope.launch {
            // Đặt trạng thái loading và xóa lỗi/thành công cũ
            _uiState.update { it.copy(isLoading = true, errorMessage = null, isRegisteredSuccess = false) }

            val currentState = _uiState.value // Lấy trạng thái hiện tại

            // --- Client-side validation ---
            if (currentState.email.isBlank() || currentState.password.isBlank() || currentState.confirmPassword.isBlank()) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Vui lòng điền đầy đủ thông tin.") }
                return@launch
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Email không hợp lệ.") }
                return@launch
            }
            if (currentState.password != currentState.confirmPassword) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Mật khẩu xác nhận không khớp.") }
                return@launch
            }
            if (!isValidPassword(currentState.password)) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ cái, chữ hoa, số và ký tự đặc biệt (!,@,#,$,%).") }
                return@launch
            }
            if (!currentState.agreeToTerms) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Bạn phải đồng ý với các điều khoản và chính sách của PathFinder.") }
                return@launch
            }

            // Nếu validation qua, tạo request và gọi repository
            val request = RegisterRequest(
                email = currentState.email,
                password = currentState.password,
                confirmPassword = currentState.confirmPassword
                // Thêm các trường khác nếu RegisterRequest của bạn có, ví dụ: name, phone...
            )

            val result = authRepository.register(request)

            result.onSuccess { user ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRegisteredSuccess = true,
                        errorMessage = null // Đảm bảo không có lỗi nếu thành công
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Đăng ký thất bại. Vui lòng thử lại."
                    )
                }
            }
        }
    }

    // Đặt lại trạng thái đăng ký thành công (để có thể đăng ký lại hoặc thoát màn hình)
    fun resetRegistrationStatus() {
        _uiState.update { it.copy(isRegisteredSuccess = false) }
    }

    // Hàm kiểm tra mật khẩu theo yêu cầu
    private fun isValidPassword(password: String): Boolean {
        // Ít nhất 8 ký tự
        if (password.length < 8) return false
        // Ít nhất một chữ cái thường
        if (!password.matches(Regex(".*[a-z].*"))) return false
        // Ít nhất một chữ cái hoa
        if (!password.matches(Regex(".*[A-Z].*"))) return false
        // Ít nhất một chữ số
        if (!password.matches(Regex(".*\\d.*"))) return false
        // Ít nhất một ký tự đặc biệt (!,@,#,$,%)
        if (!password.matches(Regex(".*[!@#$%^&*()].*"))) return false
        return true
    }
}