package com.example.pathfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.pathfinder.data.local.SessionManager
import com.example.pathfinder.di.AppContainer
import com.example.pathfinder.navigation.AppNavGraph
import com.example.pathfinder.navigation.Screen
import com.example.pathfinder.ui.theme.ApptimViecTheme
import com.example.pathfinder.utils.SettingsManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val isDark = SettingsManager.isDarkMode(this)
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
        super.onCreate(savedInstanceState)
        AppContainer.init(applicationContext)
        SettingsManager.applyThemeFromPreferences(this)

        setContent {
            ApptimViecTheme {
                val navController = rememberNavController()
                val isLoggedIn by AppContainer.sessionManager.isLoggedInFlow.collectAsState(initial = false)
                val hasProfile by AppContainer.sessionManager.hasProfileFlow.collectAsState(initial = false)

                LaunchedEffect(isLoggedIn, hasProfile) {
                    val startDestination = when {
                        isLoggedIn && hasProfile -> Screen.Home.route
                        isLoggedIn && !hasProfile -> Screen.ConfirmInfo.route
                        else -> Screen.Login.route
                    }
                    navController.navigate(startDestination) {
                        popUpTo(0)
                    }
                }
                AppNavGraph(navController = navController)
            }
        }
    }
}
