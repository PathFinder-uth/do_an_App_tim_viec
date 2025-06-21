package com.example.pathfinder.ui.screen.forgotpassword

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pathfinder.viewmodel.ForgotPasswordViewModel
import com.example.pathfinder.viewmodel.state.ForgotPasswordState

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    viewModel: ForgotPasswordViewModel
) {
    val state by viewModel.state.collectAsState()
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF67C2F6))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Quên mật khẩu",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Nhập email để gửi mail vào hộp thư của bạn để reset lại mật khẩu",
            fontSize = 14.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .height(60.dp),
            placeholder = { Text("email@domain.com") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.sendResetEmail(email) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            enabled = state !is ForgotPasswordState.Loading
        ) {
            if (state is ForgotPasswordState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
            } else {
                Text("Xác nhận", color = Color.White)
            }
        }

        if (state is ForgotPasswordState.Error) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = (state as ForgotPasswordState.Error).message, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Quay lại trang đăng nhập",
            color = Color.Blue,
            modifier = Modifier.clickable {
                navController.popBackStack()
            }
        )
    }

    LaunchedEffect(state) {
        if (state is ForgotPasswordState.Success) {
            navController.popBackStack()
            viewModel.sendResetEmail("") // optional: reset state
        }
    }
}