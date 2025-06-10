package com.example.pathfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pathfinder.di.AppContainer
import com.example.pathfinder.di.AppContainerImpl
import com.example.pathfinder.navigation.AppNavGraph
import com.example.pathfinder.navigation.Screen
import com.example.pathfinder.ui.screen.login.LoginScreen
import com.example.pathfinder.ui.theme.ApptimViecTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer: AppContainer = AppContainerImpl()
        setContent {
            ApptimViecTheme     {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavGraph(navController = navController, appContainer = appContainer)

                }
            }
        }
    }
}