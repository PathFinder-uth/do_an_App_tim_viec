package com.example.pathfinder.navigation

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pathfinder.di.AppContainer
import com.example.pathfinder.di.AppContainer.sessionManager
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
import com.example.pathfinder.ui.screen.home.HomeScreen
import com.example.pathfinder.ui.screen.recruiter.RecruiterInfoScreen
import com.example.pathfinder.ui.screen.recruiterhome.RecruiterHomeScreen
import com.example.pathfinder.ui.screen.splash.SplashScreen
import com.example.pathfinder.viewmodel.SelectUserTypeViewModel
import com.example.pathfinder.ui.screen.select.SelectUserTypeScreen
import com.example.pathfinder.viewmodel.RecruiterInfoViewModel
import com.example.pathfinder.viewmodel.state.LoginState

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object EmailVerification : Screen("email_verification")
    object ForgotPassword : Screen("forgot_password")
    object ConfirmInfo : Screen("confirm_info")
    object Profile : Screen("profile")
    object Home : Screen("home") // Tạm placeholder
    object SelectUserType : Screen("select_user_type")
    object RecruiterProfile : Screen("recruiter_profile")
    object RecruiterHome : Screen("recruiter_home")
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController, sessionManager = sessionManager)
        }

        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = viewModel(factory = AppContainer.loginViewModelFactory)
            val loginState = loginViewModel.loginState.collectAsState()

// Điều hướng sau khi đăng nhập thành công
            LaunchedEffect(loginState.value) {
                when (val state = loginState.value) {
                    is LoginState.Success -> {
                        val role = sessionManager.getSession().role
                        val route = when {
                            !state.hasProfile -> Screen.SelectUserType.route
                            role == "recruiter" -> Screen.RecruiterHome.route
                            else -> Screen.Home.route
                        }

                        navController.navigate(route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }

                        loginViewModel.resetState()
                    }
                    else -> {}
                }
            }

            LoginScreen(
                viewModel = loginViewModel,
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
            val role by sessionManager.roleFlow.collectAsState(initial = "")
            val isRecruiter = role == "recruiter"
            ConfirmInfoScreen(
                navController = navController,
                isRecruiter = isRecruiter
            )
        }

        composable(Screen.Profile.route) {
            val profileViewModel: ProfileViewModel = viewModel(factory = AppContainer.profileViewModelFactory)
            ProfileFormScreen(
                viewModel = profileViewModel,
                navController = navController)
        }

        composable(Screen.Home.route) {
            val profileViewModel: ProfileViewModel = viewModel(factory = AppContainer.profileViewModelFactory)
            val activity = LocalActivity.current as? ComponentActivity
            if (activity != null) {
                HomeScreen(
                    viewModel = profileViewModel,
                    activity = activity,
                    navController = navController,
                    sessionManager = sessionManager,
                    authRepository = AppContainer.authRepository
                )
            }
        }
        composable(Screen.SelectUserType.route) {
            val viewModel: SelectUserTypeViewModel = viewModel(factory = AppContainer.selectUserTypeViewModelFactory)
            SelectUserTypeScreen(
                viewModel = viewModel,
                onContinue = {
                    navController.navigate(Screen.ConfirmInfo.route) {
                        popUpTo(Screen.SelectUserType.route) { inclusive = true }
                    }
                },
                onBack = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.SelectUserType.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.RecruiterProfile.route) {
            val viewModel: RecruiterInfoViewModel = viewModel(factory = AppContainer.recruiterInfoViewModelFactory)
            RecruiterInfoScreen(viewModel = viewModel, navController = navController)
        }

        // ✅ Màn hình trang chủ nhà tuyển dụng
        composable(Screen.RecruiterHome.route) {
            val viewModel: RecruiterInfoViewModel = viewModel(factory = AppContainer.recruiterInfoViewModelFactory)
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.loadProfile()
            }

            RecruiterHomeScreen(
                navController = navController,
                sessionManager = sessionManager,
                authRepository = AppContainer.authRepository,
                companyName = uiState.companyName,
                logoUrl = uiState.logoUrl
            )
        }
    }
}