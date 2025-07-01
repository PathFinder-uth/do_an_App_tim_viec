package com.example.pathfinder.ui.screen.job

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.pathfinder.data.model.Job
import com.example.pathfinder.viewmodel.JobViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobScreen(
    navController: NavController,
    viewModel: JobViewModel
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchAllJobs()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Danh sách công việc", fontSize = 20.sp) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF67C2F6))
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when {
                state.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                state.error != null -> {
                    Text(
                        text = state.error ?: "Đã xảy ra lỗi",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.jobs) { job ->
                            JobCard(job = job, onClick = {
                                // TODO: Điều hướng đến màn chi tiết công việc nếu có
                                navController.navigate("job_detail/${job.id}")
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun JobCard(job: Job, onClick: () -> Unit,modifier: Modifier = Modifier) {
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val createdDate = dateFormat.format(Date(job.createdAt))
    val deadlineDate = dateFormat.format(Date(job.deadline))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // ✅ Logo và tên công ty
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (job.logoUrl.isNotBlank()) {
                    Image(
                        painter = rememberAsyncImagePainter(job.logoUrl),
                        contentDescription = "Company Logo",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = job.companyName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = job.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Địa điểm: ${job.location}", fontSize = 14.sp)
            Text(text = "Mức lương: ${job.salary}", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = job.description.take(100) + "...",
                fontSize = 12.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Ngày đăng: $createdDate", fontSize = 12.sp, color = Color.Gray)
                Text(text = "Hạn nộp: $deadlineDate", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}