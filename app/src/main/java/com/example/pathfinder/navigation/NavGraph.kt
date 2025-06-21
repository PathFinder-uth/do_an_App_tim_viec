package com.example.pathfinder.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pathfinder.di.AppContainer
import com.example.pathfinder.ui.screen.login.LoginScreen
import com.example.pathfinder.viewmodel.LoginViewModel
import com.example.pathfinder.viewmodel.RegisterViewModel
import com.example.pathfinder.viewmodel.EmailVerificationViewModel
import com.example.pathfinder.viewmodel.ForgotPasswordViewModel
import com.example.pathfinder.viewmodel.ProfileViewModel
import com.example.pathfinder.ui.screen.register.RegisterScreen
import com.example.pathfinder.ui.screen.register.EmailVerificationScreen
import com.example.pathfinder.ui.screen.forgotpassword.ForgotPasswordScreen
import com.example.pathfinder.ui.screen.confirm.ConfirmInfoScreen
import com.example.pathfinder.ui.screen.profile.ProfileFormScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object EmailVerification : Screen("email_verification")
    object ForgotPassword : Screen("forgot_password")
    object ConfirmInfo : Screen("confirm_info")
    object Profile : Screen("profile")
    object Home : Screen("home") // Tạm placeholder
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
            val loginViewModel: LoginViewModel = viewModel(factory = AppContainer.loginViewModelFactory)
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
// Điều hướng tới màn hình xác nhận thông tin
                    navController.navigate(Screen.ConfirmInfo.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onForgotPassword = {
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
        }
        composable(Screen.Register.route) {
            val registerViewModel: RegisterViewModel = viewModel(factory = AppContainer.registerViewModelFactory)
            RegisterScreen(
                navController = navController,
                viewModel = registerViewModel
            )
        }

        composable(Screen.EmailVerification.route) {
            val verificationViewModel: EmailVerificationViewModel = viewModel(factory = AppContainer.emailVerificationViewModelFactory)
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

        composable(Screen.ForgotPassword.route) {
            val forgotPasswordViewModel: ForgotPasswordViewModel = viewModel(factory = AppContainer.forgotPasswordViewModelFactory)
            ForgotPasswordScreen(
                navController = navController,
                viewModel = forgotPasswordViewModel
            )
        }

        composable(Screen.ConfirmInfo.route) {
            ConfirmInfoScreen(
                navController = navController
            )
        }

        composable(Screen.Profile.route) {
            val profileViewModel: ProfileViewModel = viewModel(factory = AppContainer.profileViewModelFactory)
            ProfileFormScreen(viewModel = profileViewModel)
        }

        composable(Screen.Home.route) {
            // Placeholder nếu cần
        }
    }
}