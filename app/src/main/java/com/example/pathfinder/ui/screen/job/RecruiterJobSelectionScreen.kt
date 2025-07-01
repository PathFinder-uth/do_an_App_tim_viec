package com.example.pathfinder.ui.screen.job

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pathfinder.data.model.Job
import com.example.pathfinder.di.AppContainer
import com.example.pathfinder.navigation.Screen
import com.example.pathfinder.viewmodel.JobViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruiterJobSelectionScreen(
    navController: NavController,
    jobViewModel: JobViewModel = viewModel(factory = AppContainer.jobViewModelFactory)
) {
    val uiState by jobViewModel.uiState.collectAsState()
    val postedJobs = uiState.jobs
    val isLoading = uiState.isLoading
    val context = LocalContext.current

    // Đồng bộ state từ ViewModel vào state cục bộ khi dữ liệu được tải
    val jobCheckedStates = remember { mutableStateMapOf<String, Boolean>() }

    // Đồng bộ state từ ViewModel vào state tạm thời khi dữ liệu được tải
    LaunchedEffect(postedJobs) {
        postedJobs.forEach { job ->
            jobCheckedStates[job.id] = job.isFilled
        }
    }

    // Khi màn hình được tải, gọi hàm để lấy các công việc của nhà tuyển dụng
    LaunchedEffect(Unit) {
        // SỬA LẠI LỜI GỌI HÀM: Lấy ID người dùng và truyền vào
        val recruiterId = FirebaseAuth.getInstance().currentUser?.uid
        if (recruiterId != null) {
            jobViewModel.fetchJobsByRecruiter(recruiterId)
        } else {
            Log.e("RecruiterJobSelection", "Không thể lấy công việc: Người dùng chưa đăng nhập.")
            // Tùy chọn: Hiển thị thông báo cho người dùng
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chọn công việc") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF67C2F6)),
                actions = {
                    TextButton(onClick = {
                        jobCheckedStates.forEach { (jobId, isChecked) ->
                            val originalState = postedJobs.find { it.id == jobId }?.isFilled
                            if (originalState != isChecked) {
                                jobViewModel.updateJobFilledStatus(jobId, isChecked)
                            }
                        }
                        Toast.makeText(context, "Đã lưu thay đổi!", Toast.LENGTH_SHORT).show()
                    }) {
                        Text("Lưu", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else if (postedJobs.isEmpty()) {
                Text("Bạn chưa đăng công việc nào.")
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(postedJobs) { job ->
                        JobItemCardForSelection(
                            job = job,
                            isChecked = jobCheckedStates[job.id] ?: job.isFilled,
                            onJobClick = {
                                navController.navigate(Screen.ApplicantJobs.withArgs(job.id))
                            },
                            onCheckedChange = { newCheckedState ->
                                jobCheckedStates[job.id] = newCheckedState
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
private fun JobItemCardForSelection(
    job: Job,
    isChecked: Boolean,
    onJobClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    val contentAlpha = if (isChecked) 0.5f else 1.0f

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onJobClick)
            .graphicsLayer(alpha = contentAlpha)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = job.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = job.companyName, style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Đủ người", style = MaterialTheme.typography.labelSmall)
            Checkbox(
                checked = isChecked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}
