package com.example.pathfinder.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pathfinder.di.AppContainer // Đảm bảo import này
import com.example.pathfinder.ui.screen.login.LoginScreen
import com.example.pathfinder.viewmodel.LoginViewModel
import com.example.pathfinder.viewmodel.LoginViewModelFactory
import com.example.pathfinder.ui.screen.register.RegisterScreen
import com.example.pathfinder.viewmodel.RegisterViewModel
import com.example.pathfinder.viewmodel.RegisterViewModelFactory
import com.example.pathfinder.ui.screen.register.EmailVerificationScreen
import com.example.pathfinder.viewmodel.EmailVerificationViewModel
import com.example.pathfinder.viewmodel.EmailVerificationViewModelFactory
import androidx.compose.material3.Text // Cần cho placeholder Home

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object EmailVerification : Screen("email_verification")
    object Home : Screen("home") // giả sử bạn có màn hình Home sau khi login
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(AppContainer.authRepository)
            )
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onForgotPassword = {
                    // Xử lý dialog hoặc điều hướng
                }
            )
        }

        composable(Screen.Register.route) {
            val registerViewModel: RegisterViewModel = viewModel(
                factory = RegisterViewModelFactory(AppContainer.authRepository) // Sử dụng appContainer đã truyền vào
            )
            RegisterScreen(
                navController = navController,
                viewModel = registerViewModel // TRUYỀN VIEWMODEL ĐÃ TẠO VÀO ĐÂY
            )
        }
        composable(Screen.EmailVerification.route) {
            val verificationViewModel: EmailVerificationViewModel = viewModel(
                factory = EmailVerificationViewModelFactory(AppContainer.authRepository)
            )
            EmailVerificationScreen(
                viewModel = verificationViewModel,
                onEmailVerified = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.EmailVerification.route) { inclusive = true }
                    }
                },
                onBackToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.EmailVerification.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            // TODO: Thêm HomeScreen sau khi đăng nhập
        }
    }
}
