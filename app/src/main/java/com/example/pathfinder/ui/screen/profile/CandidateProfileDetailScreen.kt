package com.example.pathfinder.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.pathfinder.navigation.Screen
import com.example.pathfinder.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CandidateProfileDetailScreen(
    navController: NavController,
    viewModel: ProfileViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val email = FirebaseAuth.getInstance().currentUser?.email ?: "Không có"
    LaunchedEffect(Unit) {
        viewModel.loadUserProfile() // ✅ đúng tên
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF67C2F6))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Thông tin cá nhân",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Image(
            painter = rememberAsyncImagePainter(uiState.avatarUrl),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
        )

        Spacer(modifier = Modifier.height(16.dp))

        InfoRow(label = "Họ tên", value = uiState.fullName)
        InfoRow(label = "Giới tính", value = uiState.gender)
        InfoRow(label = "Địa chỉ", value = uiState.address)
        InfoRow(label = "Số điện thoại", value = uiState.phone)
        InfoRow(label = "Ngày sinh", value = uiState.birthday)
        InfoRow(label = "Liên lạc", value = uiState.contact)
        InfoRow(label = "Email", value = email) // 👈 thêm dòng này

        Spacer(modifier = Modifier.height(32.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Quay lại")
            }

            Button(
                onClick = {
                    navController.navigate(Screen.ConfirmInfo.route)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Cập nhật", color = Color.White)
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "$label:", fontWeight = FontWeight.Medium)
        Text(text = value)
    }
}
