package com.example.pathfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppContainer.init(applicationContext)

        setContent {
            ApptimViecTheme {
                val navController = rememberNavController()

                AppNavGraph(navController = navController)
            }
        }
    }
}
