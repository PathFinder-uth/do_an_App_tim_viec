package com.example.pathfinder.data.ui.screnn.login.navigation
// com.example.pathfinder.navigation.Screen.kt

sealed class Screen(val route: String) {
    object Login : Screen("login_screen")
    object ForgotPassword : Screen("forgot_password_screen")
    object CreateAccount : Screen("create_account_screen")
    // Thêm các màn hình khác của bạn ở đây
}