package com.example.pathfinder.navigation

import android.net.Uri
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
import com.example.pathfinder.services.MyFirebaseMessagingService
import com.example.pathfinder.ui.screen.PremiumScreen
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
import com.example.pathfinder.ui.screen.job.ApplicantJobsScreen
import com.example.pathfinder.ui.screen.job.JobDetailScreen
import com.example.pathfinder.ui.screen.job.JobFormScreen
import com.example.pathfinder.ui.screen.job.JobScreen
import com.example.pathfinder.ui.screen.job.JobSearchScreen
import com.example.pathfinder.ui.screen.job.RecruiterJobSelectionScreen
import com.example.pathfinder.ui.screen.job.SavedJobsScreen
import com.example.pathfinder.ui.screen.job.SubmittedJobsScreen
import com.example.pathfinder.ui.screen.notification.NotificationsScreen
import com.example.pathfinder.ui.screen.profile.CandidateProfileDetailScreen
import com.example.pathfinder.ui.screen.profile.CandidateUpdateScreen
import com.example.pathfinder.ui.screen.recruiter.RecruiterInfoScreen
import com.example.pathfinder.ui.screen.recruiter.RecruiterProfileDetailScreen
import com.example.pathfinder.ui.screen.recruiter.RecruiterUpdateInfoScreen
import com.example.pathfinder.ui.screen.recruiterhome.RecruiterHomeScreen
import com.example.pathfinder.ui.screen.splash.SplashScreen
import com.example.pathfinder.viewmodel.SelectUserTypeViewModel
import com.example.pathfinder.ui.screen.select.SelectUserTypeScreen
import com.example.pathfinder.viewmodel.JobDatabaseViewModel
import com.example.pathfinder.viewmodel.JobSearchViewModel
import com.example.pathfinder.viewmodel.JobViewModel
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
    object JobList : Screen("job_list")
    object JobForm : Screen("job_form") {
        fun withArgs(recruiterId: String, companyName: String, logoUrl: String): String {
            return "job_form/${Uri.encode(recruiterId)}/${Uri.encode(companyName)}/${Uri.encode(logoUrl)}"
        }
    }
    object JobDetail : Screen("job_detail") {
        fun withArgs(jobId: String): String = "job_detail/${Uri.encode(jobId)}"
    }
    object CandidateDetail : Screen("candidate_detail")
    object CandidateUpdate : Screen("candidate_update")
    object RecruiterDetail : Screen("recruiter_detail")      // Xem thông tin cá nhân
    object RecruiterUpdate : Screen("recruiter_update")
    object JobSearch : Screen("job_search")
    object SavedJobs : Screen("saved_jobs")
    object SubmittedJobs : Screen("submitted_jobs")
    object ApplicantJobs : Screen("applicant_jobs/{jobId}") {
        fun withArgs(jobId: String): String = "applicant_jobs/${Uri.encode(jobId)}"
    }
    object RecruiterJobSelection : Screen("recruiter_job_selection")
    object Notifications : Screen("notifications")
    object Premium : Screen("premium")
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
                        MyFirebaseMessagingService.updateFCMToken()
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
            val activity = LocalActivity.current as? ComponentActivity
            LaunchedEffect(Unit) {
                viewModel.loadProfile()
            }
            if (activity != null) {
                RecruiterHomeScreen(
                    navController = navController,
                    activity = activity,
                    sessionManager = sessionManager,
                    authRepository = AppContainer.authRepository,
                    companyName = uiState.companyName,
                    logoUrl = uiState.logoUrl
                )
            }
        }
        composable(Screen.JobList.route) {
            val jobViewModel: JobViewModel = viewModel(factory = AppContainer.jobViewModelFactory)
            JobScreen(navController = navController, viewModel = jobViewModel)
        }
        composable(Screen.CandidateDetail.route) {
            val profileViewModel: ProfileViewModel = viewModel(factory = AppContainer.profileViewModelFactory)
            CandidateProfileDetailScreen(navController = navController, viewModel = profileViewModel)
        }

        composable(Screen.CandidateUpdate.route) {
            val profileViewModel: ProfileViewModel = viewModel(factory = AppContainer.profileViewModelFactory)
            CandidateUpdateScreen(viewModel = profileViewModel, navController = navController)
        }
        composable(Screen.RecruiterDetail.route) {
            val viewModel: RecruiterInfoViewModel = viewModel(factory = AppContainer.recruiterInfoViewModelFactory)
            RecruiterProfileDetailScreen(viewModel = viewModel, navController = navController)
        }

        composable(Screen.RecruiterUpdate.route) {
            val viewModel: RecruiterInfoViewModel = viewModel(factory = AppContainer.recruiterInfoViewModelFactory)
            RecruiterUpdateInfoScreen(viewModel = viewModel, navController = navController)
        }
        composable(Screen.JobSearch.route) {
            val viewModel: JobSearchViewModel = viewModel(factory = AppContainer.jobSearchViewModelFactory)
            JobSearchScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.Notifications.route) {
            NotificationsScreen(navController = navController)
        }
        composable(Screen.Premium.route) { PremiumScreen(navController) }
        composable(Screen.SavedJobs.route) {
            // Sử dụng JobDatabaseViewModel thay vì JobViewModel
            val jobDatabaseViewModel: JobDatabaseViewModel = viewModel(factory = AppContainer.jobDatabaseViewModelFactory)
            // Truyền jobDatabaseViewModel vào SavedJobsScreen
            SavedJobsScreen(navController = navController, )
        }

        composable(Screen.SubmittedJobs.route) {
            val jobDatabaseViewModel: JobDatabaseViewModel = viewModel(factory = AppContainer.jobDatabaseViewModelFactory)

            // Truyền vào ViewModel và các dữ liệu cần thiết cho màn hình "Đơn đã nộp"
            SubmittedJobsScreen(
                navController = navController,
                onJobClicked = { jobId ->
                    // Thực hiện xử lý khi người dùng nhấn vào công việc, ví dụ: điều hướng tới màn hình chi tiết công việc
                    navController.navigate(Screen.JobDetail.withArgs(jobId))
                }
            )
        }

        composable(Screen.ApplicantJobs.route) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
            ApplicantJobsScreen(jobId = jobId, navController = navController)
        }
        composable(Screen.RecruiterJobSelection.route) {
            RecruiterJobSelectionScreen(navController = navController)
        }
        composable(
            route = "job_form/{recruiterId}/{companyName}/{logoUrl}"
        ) { backStackEntry ->
            val recruiterId = Uri.decode(backStackEntry.arguments?.getString("recruiterId") ?: "")
            val companyName = Uri.decode(backStackEntry.arguments?.getString("companyName") ?: "")
            val logoUrl = Uri.decode(backStackEntry.arguments?.getString("logoUrl") ?: "")

            JobFormScreen(
                recruiterId = recruiterId,
                companyName = companyName,
                logoUrl = logoUrl,
                viewModel = viewModel(factory = AppContainer.jobViewModelFactory),
                navController = navController
            )
        }

        composable("job_detail/{jobId}") { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
            val viewModel: JobViewModel = viewModel(factory = AppContainer.jobViewModelFactory)
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(jobId) {
                viewModel.getJobDetail(jobId)
            }

            uiState.selectedJob?.let { job ->
                JobDetailScreen(job = job, jobId = jobId)
            }
        }
    }
}