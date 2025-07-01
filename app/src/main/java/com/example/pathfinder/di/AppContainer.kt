package com.example.pathfinder.di

import android.content.Context
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Room
import com.example.pathfinder.data.dao.JobDao
import com.example.pathfinder.data.database.AppDatabase
import com.example.pathfinder.data.remote.FirebaseAuthServiceImpl
import com.example.pathfinder.data.remote.FirebaseUserServiceImpl
import com.example.pathfinder.data.remote.IAuthService
import com.example.pathfinder.data.remote.IFirebaseUserService
import com.example.pathfinder.data.repository.AuthRepository
import com.example.pathfinder.data.repository.ProfileRepository
import com.example.pathfinder.viewmodel.*
import com.example.pathfinder.data.local.SessionManager
import com.example.pathfinder.data.remote.AlgoliaSearchService
import com.example.pathfinder.data.remote.FirebaseJobServiceImpl
import com.example.pathfinder.data.remote.FirebaseRecruiterServiceImpl
import com.example.pathfinder.data.remote.IRecruiterService
import com.example.pathfinder.data.repository.JobRepository
import com.example.pathfinder.viewmodel.JobViewModelFactory as externalJobFactory
import androidx.compose.ui.platform.LocalContext
object AppContainer {
    private lateinit var internalSessionManager: SessionManager
    val sessionManager: SessionManager
        get() = internalSessionManager
    // Firebase service
    val authService: IAuthService = FirebaseAuthServiceImpl()
    val userService: IFirebaseUserService = FirebaseUserServiceImpl()
    val recruiterService: IRecruiterService = FirebaseRecruiterServiceImpl()
    val jobRepository = JobRepository(FirebaseJobServiceImpl())
    val algoliaSearchService = AlgoliaSearchService()
    // Repositories
    val authRepository = AuthRepository(authService)
    val profileRepository = ProfileRepository(userService, recruiterService)
    lateinit var loginViewModelFactory: LoginViewModelFactory
        private set
    // ViewModel Factories
    val selectUserTypeViewModelFactory: SelectUserTypeViewModelFactory by lazy {
        SelectUserTypeViewModelFactory(sessionManager, userService)
    }
    val recruiterInfoViewModelFactory by lazy {
        RecruiterInfoViewModelFactory(profileRepository)
    }

    private lateinit var appDatabase: AppDatabase
    val jobDao: JobDao
        get() = appDatabase.jobDao()
    val jobDatabaseViewModelFactory: JobDatabaseViewModelFactory by lazy {
        JobDatabaseViewModelFactory(jobDao)
    }

    val registerViewModelFactory = RegisterViewModelFactory(authRepository)
    val emailVerificationViewModelFactory = EmailVerificationViewModelFactory(authRepository)
    val forgotPasswordViewModelFactory = ForgotPasswordViewModelFactory(authRepository)
    val profileViewModelFactory = ProfileViewModelFactory(profileRepository)
    fun init(context: Context) {
        internalSessionManager = SessionManager(context.applicationContext)
        loginViewModelFactory = LoginViewModelFactory(authRepository, userService, sessionManager)
        appDatabase = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "pathfinder_database"  // Đảm bảo tên đúng với database của bạn
        ).build()
    }

    val jobViewModelFactory = externalJobFactory
    val jobSearchViewModelFactory = JobSearchViewModelFactory(algoliaSearchService)
}