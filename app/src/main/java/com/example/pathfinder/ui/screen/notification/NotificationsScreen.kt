package com.example.pathfinder.ui.screen.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pathfinder.data.model.Notification
import com.example.pathfinder.viewmodel.NotificationViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    navController: NavController,
    viewModel: NotificationViewModel = viewModel()
) {
    val notifications by viewModel.notifications.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)

    // Bắt đầu lắng nghe thông báo khi màn hình được hiển thị
    LaunchedEffect(Unit) {
        viewModel.listenForNotifications()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thông báo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF67C2F6))
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else if (notifications.isEmpty()) {
                Text("Bạn chưa có thông báo nào.")
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(notifications) { notification ->
                        NotificationItem(
                            notification = notification,
                            onClick = {
                                // Khi nhấn vào, đánh dấu là đã đọc
                                viewModel.markAsRead(notification.id)
                                // TODO: Có thể điều hướng đến màn hình chi tiết công việc
                            }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationItem(notification: Notification, onClick: () -> Unit) {
    // Thay đổi màu nền cho thông báo chưa đọc
    val backgroundColor = if (notification.isRead) Color.White else Color(0xFFE3F2FD)
    val dateFormat = remember { SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = notification.senderName,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            notification.timestamp?.let {
                Text(
                    text = dateFormat.format(it),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = notification.message,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}