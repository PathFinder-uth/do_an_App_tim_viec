package com.example.pathfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.pathfinder.navigation.AppNavGraph
import com.example.pathfinder.ui.theme.ApptimViecTheme// thay bằng tên theme của bạn

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ApptimViecTheme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}