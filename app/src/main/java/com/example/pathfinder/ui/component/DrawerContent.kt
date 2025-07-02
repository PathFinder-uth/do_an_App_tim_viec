package com.example.pathfinder.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

@Composable
fun DrawerContent(
    fullName: String,
    avatarUrl: String,
    onLogout: () -> Unit,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color(0xFFF9F2FF))
            .padding(16.dp)
    ) {
        // Avatar + Name
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = rememberAsyncImagePainter(avatarUrl),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = fullName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        val items = listOf(
            "Thông tin tài khoản" to Icons.Default.Person,
            "Thông báo tuyển dụng" to Icons.Default.Notifications,
            "Đơn đã nộp" to Icons.Default.CheckCircle,
            "Danh sách lưu" to Icons.Default.Star,
            "Mua gói" to Icons.Default.ShoppingCart,
            "Hỗ trợ" to Icons.Default.Help,
            "Cài đặt" to Icons.Default.Settings,
            "Đăng xuất" to Icons.Default.Logout,
        )

        items.forEach { (label, icon) ->
            Button(
                onClick = {
                    when (label) {
                        "Đăng xuất" -> onLogout()
                        "Thông tin tài khoản" -> navController.navigate("candidate_detail")
                        "Danh sách lưu" -> navController.navigate(Screen.SavedJobs.route) // Điều hướng đến SavedJobsScreen// hoặc Screen.CandidateDetail.route
                        "Đơn đã nộp" -> navController.navigate(Screen.SubmittedJobs.route)
                        "Thông báo tuyển dụng" -> navController.navigate(Screen.Notifications.route)
                        "Mua gói" -> navController.navigate(Screen.Premium.route)
                        "Hỗ trợ" ->  navController.navigate(Screen.Support.route)
                        "Cài đặt" -> navController.navigate(Screen.Settings.route)
                        else -> {} // TODO: Other actions
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black, // màu icon và text
                    disabledContainerColor = Color.LightGray // khi bấm sẽ thấy rõ hơn
                ),
                shape = RoundedCornerShape(6.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Icon(imageVector = icon, contentDescription = label, tint = Color.Black)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = label, color = Color.Black)
            }
        }
    }
}