package com.example.pathfinder.ui.screen.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.pathfinder.viewmodel.EmailVerificationViewModel
import com.example.pathfinder.viewmodel.state.EmailVerificationState

@Composable
fun EmailVerificationScreen(
    viewModel: EmailVerificationViewModel,
    onEmailVerified: () -> Unit,
    onBackToLogin: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    // Tự động chuyển khi xác minh xong
    LaunchedEffect(state) {
        if (state is EmailVerificationState.Verified) {
            onEmailVerified()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF67C2F6)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text("Đăng ký tài khoản", fontSize = 20.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Xác nhận Email", fontSize = 24.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Email xác minh đã được gửi đến email của bạn. Vui lòng kiểm tra hộp thư và nhấn vào liên kết xác minh.",
                fontSize = 14.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.checkEmailVerified() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Tôi đã xác minh", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Gửi lại email xác minh",
                color = Color.Blue,
                modifier = Modifier.clickable { viewModel.sendVerificationEmail() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (state is EmailVerificationState.Error) {
                Text(
                    text = (state as EmailVerificationState.Error).message,
                    color = Color.Red,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Trở về đăng nhập",
                color = Color.Blue,
                modifier = Modifier.clickable { onBackToLogin() }
            )
        }
    }
}