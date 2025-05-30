package com.example.pathfinder.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pathfinder.data.remote.FirebaseAuthServiceImpl
import com.example.pathfinder.data.repository.AuthRepository
import com.example.pathfinder.di.AppContainer
import com.example.pathfinder.ui.screen.login.LoginScreen
import com.example.pathfinder.viewmodel.LoginViewModel
import com.example.pathfinder.viewmodel.LoginViewModelFactory


sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
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
            // TODO: Thêm RegisterScreen sau
        }

        composable(Screen.Home.route) {
            // TODO: Thêm HomeScreen sau khi đăng nhập
        }
    }
}
