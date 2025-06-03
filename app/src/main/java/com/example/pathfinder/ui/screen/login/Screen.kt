package com.example.pathfinder.ui.screen.login


sealed class Screen(val route: String) {
    object Login : Screen("login_screen")
    object ForgotPassword : Screen("forgot_password_screen")
    object CreateAccount : Screen("create_account_screen")
    // Thêm các màn hình khác của bạn ở đây
    object Register: Screen("register")
}