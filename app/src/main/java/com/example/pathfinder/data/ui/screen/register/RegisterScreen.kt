package com.example.pathfinder.ui.screen.register // Chú ý: package của bạn là data.ui.screen.register, tôi đã sửa lại thành ui.screen.register cho đúng với cấu trúc mẫu

import android.util.Patterns // Đảm bảo import này vẫn còn nếu dùng trong Composable, nhưng tốt nhất là trong ViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.* // Sử dụng Material Design 3
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pathfinder.ui.theme.ApptimViewTheme
import androidx.navigation.NavController
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel // Import này quan trọng
import androidx.navigation.compose.rememberNavController
import com.example.pathfinder.data.model.LoginRequest
import com.example.pathfinder.data.model.RegisterRequest
import com.example.pathfinder.data.model.User
import com.example.pathfinder.data.remote.IAuthService
import com.example.pathfinder.data.repository.AuthRepository
import com.example.pathfinder.di.AppContainer // Import AppContainer của bạn
import com.example.pathfinder.navigation.Screen // Nếu bạn có định nghĩa Screen cho điều hướng
import com.example.pathfinder.viewmodel.RegisterViewModel // Import ViewModel
import com.example.pathfinder.viewmodel.RegisterViewModelFactory // Import ViewModelFactory

// Xóa các import không cần thiết hoặc trùng lặp từ Material2:
// import androidx.compose.material.*
// import androidx.compose.material.icons.filled.Check // Không dùng trực tiếp Icon Checkbox nữa

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel// Thêm tham số này để truyền AppContainer
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isRegisteredSuccess) {
        if (uiState.isRegisteredSuccess) {
            navController.navigate(Screen.Login.route) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
            viewModel.resetRegistrationStatus()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF7DE4F5))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Đăng ký tài khoản",
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(130.dp))

        Text(
            text = "Nhập Email",
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = uiState.email, // Lấy giá trị từ uiState
            onValueChange = viewModel::onEmailChange, // Gọi hàm trong ViewModel
            placeholder = { Text("email@domain.com") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .height(70.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true // Thêm để tránh lỗi nhiều dòng
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Nhập Password",
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(5.dp))

        OutlinedTextField(
            value = uiState.password, // Lấy giá trị từ uiState
            onValueChange = viewModel::onPasswordChange, // Gọi hàm trong ViewModel
            placeholder = { Text("password") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .height(70.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(), // Ẩn mật khẩu
            singleLine = true
        )

        Text(
            text = "Độ dài mật khẩu có ít nhất 8 kí tự, phải bao gồm chữ cái, chữ hoa, số và ký tự đặc biệt (!,@,#,$,%)",
            fontSize = 12.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(top = 4.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Xác nhận lại Password",
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = uiState.confirmPassword, // Lấy giá trị từ uiState
            onValueChange = viewModel::onConfirmPasswordChange, // Gọi hàm trong ViewMode
            placeholder = { Text("password") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .height(70.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(), // Ẩn mật khẩu
            singleLine = true
        )

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically, // Căn giữa chiều dọc
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White).height(70.dp)
                .clickable { viewModel.onAgreeToTermsChange(!uiState.agreeToTerms) } // Cập nhật trạng thái trong ViewModel
        ) {
            // Sử dụng Material3 Checkbox
            Checkbox(
                modifier = Modifier.align(Alignment.Top),
                checked = uiState.agreeToTerms, // Lấy giá trị từ uiState
                onCheckedChange = viewModel::onAgreeToTermsChange, // Gọi hàm trong ViewModel
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Đồng ý với các điều khoản và chính sách của PathFinder",
                fontSize = 16.sp,
                modifier = Modifier.weight(1f) // Text sẽ chiếm phần còn lại
            )
        }

        // Hiển thị thông báo lỗi từ ViewModel
        uiState.errorMessage?.let { message ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

        Button(
            onClick = viewModel::register, // Gọi hàm register trong ViewModel
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(bottom = 10.dp), // background(Color.Black) ở đây không có tác dụng nếu có shape
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            enabled = !uiState.isLoading // Nút bị vô hiệu hóa khi đang loading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(text = "Continue", color = Color.White)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    ApptimViewTheme {
        val mockAppContainer = object : AppContainer {
            // Đảm bảo import đúng cho IAuthService
            override val authService: IAuthService
                get() = object : IAuthService {
                    // Triển khai tất cả các hàm abstract của IAuthService VỚI CHỮ KÝ HÀM ĐÚNG
                    override suspend fun login(email: String, password: String): Result<User> {
                        // Trả về một Result.success hoặc Result.failure giả cho Preview
                        return Result.success(User("preview_login_uid", email))
                    }

                    override suspend fun register(request: RegisterRequest): Result<User> { // SỬA CHỮ KÝ HÀM Ở ĐÂY
                        // Trả về một Result.success hoặc Result.failure giả cho Preview
                        // Bạn có thể dùng request.email, request.password để tạo User giả
                        return Result.success(User("preview_register_uid", request.email))
                    }

                    override suspend fun loginWithEmail(loginRequest: LoginRequest): Result<User> {
                        return Result.success(User("preview_login_with_email_uid", loginRequest.email))
                    }

                    override suspend fun loginWithGoogle(idToken: String): Result<User> {
                        return Result.success(User("preview_google_uid", "google@example.com"))
                    }

                    override suspend fun loginWithFacebook(token: String): Result<User> {
                        return Result.success(User("preview_facebook_uid", "facebook@example.com"))
                    }

                    override fun getCurrentUser(): User? {
                        return User("preview_current_uid", "preview@example.com")
                    }

                    override fun logout() {
                        // Mock logout logic
                    }
                }
            // Đảm bảo import đúng cho AuthRepository
            override val authRepository: AuthRepository
                get() = AuthRepository(authService)
            // Đảm bảo import đúng cho LoginViewModelFactory
            override val loginViewModelFactory: com.example.pathfinder.viewmodel.LoginViewModelFactory
                get() = com.example.pathfinder.viewmodel.LoginViewModelFactory(authRepository)
            // Đảm bảo import đúng cho RegisterViewModelFactory
            override val registerViewModelFactory: RegisterViewModelFactory
                get() = RegisterViewModelFactory(authRepository)
        }

        val previewRegisterViewModel: RegisterViewModel = viewModel(
            factory = mockAppContainer.registerViewModelFactory
        )
        RegisterScreen(navController = rememberNavController(), viewModel = previewRegisterViewModel)
    }
}