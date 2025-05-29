package com.example.pathfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pathfinder.ui.theme.ApptimViecTheme
import androidx.compose.material3.Surface // Hoặc androidx.compose.material.Surface nếu bạn dùng Material 2
import androidx.compose.material3.MaterialTheme // Hoặc androidx.compose.material.MaterialTheme nếu bạn dùng Material 2
import androidx.navigation.compose.rememberNavController // Để khởi tạo NavHostController
import com.example.pathfinder.data.ui.screnn.login.navigation.AppNavGraph // Để gọi NavGraph của bạn
// Đảm bảo "com.example.pathfinder.navigation" là package chính xác cho file NavGraph.kt của bạn

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ApptimViecTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    /*modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background*/
                ) {
                    /*val navController = rememberNavController()
                AppNavGraph(navController = navController)*/
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ApptimViecTheme {
        Greeting("Android")
    }
}