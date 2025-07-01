package com.example.pathfinder.ui.screen.recruiterhome

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.pathfinder.data.local.SessionManager
import com.example.pathfinder.data.repository.AuthRepository
import com.example.pathfinder.navigation.Screen
import com.example.pathfinder.ui.component.RecruiterDrawerContent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruiterHomeScreen(
    navController: NavController,
    activity: ComponentActivity,
    sessionManager: SessionManager,
    authRepository: AuthRepository,
    companyName: String,
    logoUrl: String
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler {
        showExitDialog = true
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Xác nhận thoát") },
            text = { Text("Bạn có muốn thoát ứng dụng không?") },
            confirmButton = {
                TextButton(onClick = { activity.finish() }) {
                    Text("Xác nhận")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }

    ModalNavigationDrawer(
        drawerContent = {
            RecruiterDrawerContent(
                companyName = companyName,
                logoUrl = logoUrl,
                onMenuClick = { label ->
                    when (label) {
                        "Thông tin tài khoản" -> navController.navigate(Screen.RecruiterProfile.route)
                        "+ Tạo bài đăng tuyển dụng mới" -> {
                            // TODO: Điều hướng đến màn hình tạo bài đăng
                        }
                        "Xem danh sách bài đăng đã đào tạo" -> {
                            // TODO: Điều hướng đến màn danh sách bài đăng
                        }
                        "Xem danh sách ứng viên đã nộp đơn" -> {
                            // TODO: Điều hướng đến danh sách ứng viên
                        }
                        "Hỗ trợ" -> {
                            // TODO: Mở support (hoặc external link)
                        }
                        "Cài đặt" -> {
                            // TODO: Mở màn hình cài đặt nếu có
                        }
                        "Đăng xuất" -> {
                            scope.launch {
                                authRepository.logout()
                                sessionManager.clearSession()
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(0)
                                }
                            }
                        }
                    }
                }
            )
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("PathFinder", fontWeight = FontWeight.Bold, fontSize = 24.sp) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF67C2F6)),
                    actions = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            containerColor = Color(0xFF67C2F6)
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "\"Connecting talent with opportunity\"",
                        fontSize = 14.sp,
                        fontStyle = FontStyle.Italic,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Danh sách công việc",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            .background(Color.White, RoundedCornerShape(8.dp))
                    )
                }
            }
        }
    }
}