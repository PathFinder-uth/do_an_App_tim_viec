// com.example.pathfinder.navigation.NavGraph.kt
package com.example.pathfinder.data.ui.screnn.login.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pathfinder.ui.screen.login.LoginScreen // Import LoginScreen

@Composable
fun AppNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route // Đặt màn hình đăng nhập làm màn hình khởi đầu
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    // Ví dụ: điều hướng đến màn hình chính sau khi đăng nhập thành công
                    // navController.navigate(Screen.Home.route) {
                    //     popUpTo(Screen.Login.route) { inclusive = true } // Xóa màn hình đăng nhập khỏi stack
                    // }
                },
                onForgotPasswordClick = {
                    navController.navigate(Screen.ForgotPassword.route)
                },
                onCreateAccountClick = {
                    navController.navigate(Screen.CreateAccount.route)
                }
            )
        }

        composable(route = Screen.ForgotPassword.route) {
            // TODO: Tạo màn hình ForgotPasswordScreen.kt và gọi nó ở đây

        }

        composable(route = Screen.CreateAccount.route) {
            // TODO: Tạo màn hình CreateAccountScreen.kt và gọi nó ở đây

        }

        // Thêm các composable khác cho các màn hình khác
    }
}