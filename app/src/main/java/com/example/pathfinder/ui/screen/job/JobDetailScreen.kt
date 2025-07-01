package com.example.pathfinder.ui.screen.job

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.pathfinder.data.model.Job
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pathfinder.data.model.JobEntity
import com.example.pathfinder.di.AppContainer
import com.example.pathfinder.ui.screen.cv.UploadCvScreen
import com.example.pathfinder.viewmodel.JobDatabaseViewModel
import com.example.pathfinder.viewmodel.JobDatabaseViewModelFactory
import com.example.pathfinder.viewmodel.JobViewModel
import kotlinx.coroutines.launch
import com.example.pathfinder.viewmodel.CvUploadViewModel
import com.example.pathfinder.viewmodel.SubmittedCVViewModel
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailScreen(job: Job, jobId: String, jobViewModel: JobViewModel = viewModel()) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val createdAtStr = dateFormat.format(Date(job.createdAt))
    val deadlineStr = dateFormat.format(Date(job.deadline))

    val jobDao = AppContainer.jobDao  // Lấy JobDao từ AppContainer
    val cvUploadViewModel: CvUploadViewModel = viewModel()
    val submittedCVViewModel: SubmittedCVViewModel = viewModel()
    // Khởi tạo ViewModel với JobDatabaseViewModelFactory
    val jobDatabaseViewModel: JobDatabaseViewModel = viewModel(
        factory = JobDatabaseViewModelFactory(jobDao)
    )
    var showUploadCvScreen by remember { mutableStateOf(false) }

    var pdfUrl by remember { mutableStateOf<String?>(null) }
    val hasApplied by submittedCVViewModel.hasApplied.collectAsState()
    val uploadState by cvUploadViewModel.uploadState.collectAsState()

    var isCvSubmitted by remember { mutableStateOf(false) }
    val context = LocalContext.current
    // 👇 Tính số ngày còn lại
    val now = System.currentTimeMillis()
    val remainingMillis = job.deadline - now
    val remainingDays = (remainingMillis / (1000 * 60 * 60 * 24)).toInt()
    val deadlineNote = when {
        remainingDays < 0 -> "⛔ Đã hết hạn"
        remainingDays == 0 -> "⚠ Hôm nay là hạn chót"
        else -> "🕒 Còn $remainingDays ngày đến hạn nộp"
    }
    LaunchedEffect(key1 = job.id) {
        submittedCVViewModel.checkIfAlreadyApplied(job.id)
    }

    // Khi rời màn hình, reset trạng thái để không ảnh hưởng đến lần xem công việc sau
    DisposableEffect(Unit) {
        onDispose {
            submittedCVViewModel.resetApplicationStatus()
        }
    }
    LaunchedEffect(uploadState) {
        when (val state = uploadState) {
            is CvUploadViewModel.UploadState.Success -> {
                showUploadCvScreen = false


                // GỌI HÀM MỚI TỪ VIEWMODEL
                submittedCVViewModel.submitCvApplication(
                    jobId = job.id,
                    cvUrl = state.url,
                    jobTitle = job.title,
                    onSuccess = {
                        // Xử lý khi thành công (lưu vào db local, báo snackbar...)
                        val jobEntity = JobEntity(
                            id = job.id, recruiterId = job.recruiterId, title = job.title,
                            description = job.description, requirements = job.requirements,
                            salary = job.salary, location = job.location, type = job.type,
                            companyName = job.companyName, logoUrl = job.logoUrl,
                            createdAt = job.createdAt, deadline = job.deadline,
                            isCvSubmitted = true
                        )
                        jobDatabaseViewModel.saveJob(jobEntity)

                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("CV đã được nộp thành công!")
                        }
                    },
                    onError = { errorMessage ->
                        // Xử lý khi có lỗi
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(errorMessage)
                        }
                    }
                )
            }
            else -> {}
        }
    }

    // Màn hình upload CV
    if (showUploadCvScreen) {
        // SỬA LẠI LỜI GỌI HÀM, BỎ `jobId` và `onUploadSuccess`
        UploadCvScreen(
            onCancel = { showUploadCvScreen = false } // Đóng màn hình upload CV
        )
    } else {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = { Text(text = "Chi tiết công việc") },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF67C2F6))
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    if (job.logoUrl.isNotBlank()) {
                        Image(
                            painter = rememberAsyncImagePainter(job.logoUrl),
                            contentDescription = "Logo công ty",
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = job.companyName,
                        fontSize = 18.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = job.title,
                    fontSize = 20.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                InfoRow("Địa điểm", job.location)
                InfoRow("Mức lương", job.salary)
                InfoRow("Hình thức làm việc", job.type)

                InfoRow("Ngày đăng", createdAtStr)
                InfoRow("Hạn nộp", deadlineStr)

                // 👇 Hiển thị trạng thái hạn nộp
                Text(
                    text = deadlineNote,
                    color = if (remainingDays < 1) Color.Red else Color(0xFF388E3C),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Mô tả công việc",
                    fontSize = 16.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                )
                Text(job.description, fontSize = 14.sp)

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Yêu cầu",
                    fontSize = 16.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                )
                Text(job.requirements, fontSize = 14.sp)

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Chỉ cho phép nộp CV nếu chưa nộp CV trước đó
                    Button(
                        onClick = {
                            if (hasApplied == true) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Bạn đã nộp CV cho công việc này rồi.")
                                }
                            } else {
                                cvUploadViewModel.resetUploadState()
                                showUploadCvScreen = true
                            }
                        },
                        // Nút bị vô hiệu hóa trong khi đang kiểm tra
                        enabled = hasApplied != null,
                        modifier = Modifier.border(1.dp, Color.Black, CircleShape).clip(CircleShape),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        val buttonText = when (hasApplied) {
                            true -> "Đã nộp CV"
                            false -> "Nộp CV"
                            else -> "Đang kiểm tra..."
                        }
                        Text(text = buttonText, color = Color(0xFF000000))
                    }


                    Button(
                        onClick = {
                            // Chuyển Job sang JobEntity và gọi saveJob
                            val jobEntity = JobEntity(
                                id = job.id,
                                recruiterId = job.recruiterId,
                                title = job.title,
                                description = job.description,
                                requirements = job.requirements,
                                salary = job.salary,
                                location = job.location,
                                type = job.type,
                                companyName = job.companyName,
                                logoUrl = job.logoUrl,
                                createdAt = job.createdAt,
                                deadline = job.deadline,
                                isCvSubmitted = hasApplied ?: false
                            )
                            jobDatabaseViewModel.saveJob(jobEntity)

                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    "Công việc đã được lưu!",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        },
                        modifier = Modifier
                            .border(1.dp, Color.Black, CircleShape)
                            .clip(CircleShape),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text(text = "Lưu công việc", color = Color(0xFF000000))
                    }
                }
            }
        }
    }
}
@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = "$label: ", fontWeight = androidx.compose.ui.text.font.FontWeight.Medium)
        Text(text = value)
    }
}