package com.example.pathfinder.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

@Composable
fun RecruiterDrawerContent(
    recruiterId: String, // 🆕 Thêm vào đây
    companyName: String,
    logoUrl: String,
    onMenuClick: (String, recruiterId: String, companyName: String, logoUrl: String) -> Unit // 🆕 Đổi callback để truyền đủ
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color(0xFFF9F2FF))
            .padding(16.dp)
    ) {
        // Avatar + Company name
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = rememberAsyncImagePainter(logoUrl),
                contentDescription = "Company Logo",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = companyName, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        val items = listOf(
            "Thông tin tài khoản" to Icons.Default.Person,
            "Tạo bài đăng tuyển dụng mới" to Icons.Default.Add,
            "Xem danh sách bài đăng đã đào tạo" to Icons.Default.School,
            "Xem danh sách ứng viên đã nộp đơn" to Icons.Default.CheckCircle,
            "Hỗ trợ" to Icons.Default.Help,
            "Cài đặt" to Icons.Default.Settings,
            "Đăng xuất" to Icons.Default.Logout,
        )

        items.forEach { (label, icon) ->
            Button(
                onClick = {
                    onMenuClick(label, recruiterId, companyName, logoUrl) // 🆕 Truyền đủ tham số
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(6.dp),
                elevation = ButtonDefaults.buttonElevation(2.dp)
            ) {
                Icon(imageVector = icon, contentDescription = label)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = label)
            }
        }
    }
}
