package com.example.pathfinder.ui.screen.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.example.pathfinder.R
import com.example.pathfinder.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, isLoggedIn: Boolean,
                 hasProfile: Boolean) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.office_walk))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1
    )

    var showSlogan by remember { mutableStateOf(false) }
    var titleVisible by remember { mutableStateOf(false) }

    val titleAlpha by animateFloatAsState(
        targetValue = if (titleVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 1200)
    )

    val pathfinderFont = FontFamily(Font(R.font.poppins_bold))
    val sloganFont = FontFamily(Font(R.font.greatvibes_regular))

    LaunchedEffect(isLoggedIn, hasProfile) {
        if (isLoggedIn != null && hasProfile != null) {
            titleVisible = true
            delay(800) // Cho animation
            showSlogan = true
            delay(3000)

            val nextRoute = when {
                isLoggedIn!! && hasProfile!! -> Screen.Home.route
                isLoggedIn!! && !hasProfile!! -> Screen.ConfirmInfo.route
                else -> Screen.Login.route
            }

            navController.navigate(nextRoute) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3E5FC)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(32.dp))

            // Row with logo and title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.alpha(titleAlpha)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_small),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Pathfinder",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = pathfinderFont,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Lottie animation
            LottieAnimation(
                composition,
                progress,
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedVisibility(
                visible = showSlogan,
                enter = fadeIn(animationSpec = tween(durationMillis = 1500))
            ) {
                Text(
                    text = "The smarter way to find your career.",
                    fontSize = 22.sp,
                    color = Color(0xFF1A237E),
                    fontFamily = sloganFont,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(horizontal = 32.dp),
                    style = TextStyle(letterSpacing = 1.sp)
                )
            }

        }
    }
}
