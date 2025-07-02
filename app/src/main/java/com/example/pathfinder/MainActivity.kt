package com.example.pathfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.pathfinder.data.local.SessionManager
import com.example.pathfinder.di.AppContainer
import com.example.pathfinder.navigation.AppNavGraph
import com.example.pathfinder.navigation.Screen
import com.example.pathfinder.ui.theme.ApptimViecTheme
import com.example.pathfinder.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppContainer.init(applicationContext)

        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()
            ApptimViecTheme (settingsViewModel = settingsViewModel){
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    val navController = rememberNavController()

                    // 3. Truyền ViewModel này vào NavGraph để các màn hình con có thể sử dụng
                    AppNavGraph(
                        navController = navController,
                        settingsViewModel = settingsViewModel
                    )
                }
            }
        }
    }
}
