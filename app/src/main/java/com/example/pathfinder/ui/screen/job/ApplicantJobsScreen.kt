package com.example.pathfinder.ui.screen.job

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.pathfinder.data.model.SubmittedCV
import com.example.pathfinder.di.AppContainer
import com.example.pathfinder.viewmodel.RecruiterInfoViewModel
import com.example.pathfinder.viewmodel.SubmittedCVViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicantJobsScreen(jobId: String, viewModel: SubmittedCVViewModel = viewModel(), navController: NavController) {
    val submittedCVs by viewModel.submittedCVs.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val downloadState by viewModel.downloadState.observeAsState(SubmittedCVViewModel.DownloadState.Idle)
    val context = LocalContext.current
    val cvCheckedStates = remember { mutableStateMapOf<String, Boolean>() }
    val recruiterInfoViewModel: RecruiterInfoViewModel = viewModel(factory = AppContainer.recruiterInfoViewModelFactory)
    val recruiterState by recruiterInfoViewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        recruiterInfoViewModel.loadProfile()
    }
    // Đồng bộ state từ ViewModel vào state tạm thời khi dữ liệu được tải
    LaunchedEffect(submittedCVs) {
        submittedCVs?.forEach { cv ->
            cvCheckedStates[cv.id] = cv.isReviewed
        }
    }

    LaunchedEffect(jobId) {
        viewModel.fetchSubmittedCVs(jobId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Danh sách ứng viên") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF67C2F6)),
                actions = {
                    // Nút Lưu để lưu trạng thái checkbox
                    TextButton(onClick = {
                        cvCheckedStates.forEach { (cvId, isChecked) ->
                            val originalState = submittedCVs?.find { it.id == cvId }?.isReviewed
                            if (originalState != isChecked) {
                                viewModel.updateCvReviewedStatus(cvId, isChecked)
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
            } else if (submittedCVs.isNullOrEmpty()) {
                Text("Chưa có ứng viên nào nộp CV.")
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(submittedCVs!!) { submittedCV ->
                        val isTicked = cvCheckedStates[submittedCV.id] ?: submittedCV.isReviewed
                        val contentAlpha = if (isTicked) 0.5f else 1.0f

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer(alpha = contentAlpha)
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Ứng viên: ${submittedCV.applicantName}")
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Công việc: ${submittedCV.jobTitle}")
                            }

                            // --- SỬA LẠI LỜI GỌI HÀM ---
                            IconButton(onClick = {
                                viewModel.sendNotificationToApplicant(
                                    applicantId = submittedCV.applicantId,
                                    jobTitle = submittedCV.jobTitle,
                                    companyName = recruiterState.companyName // Lấy tên công ty thật
                                )
                                Toast.makeText(context, "Đã gửi thông báo!", Toast.LENGTH_SHORT).show()
                            }) {
                                Icon(Icons.Default.Notifications, contentDescription = "Gửi thông báo")
                            }

                            if (submittedCV.cvUrl.isNotBlank()) {
                                Button(onClick = { viewModel.downloadCV(context, submittedCV) }) {
                                    Text("Xem CV")
                                }
                            }

                            Checkbox(
                                checked = isTicked,
                                onCheckedChange = { newCheckedState ->
                                    cvCheckedStates[submittedCV.id] = newCheckedState
                                }
                            )
                        }
                        Divider()
                    }
                }
            }
        }
    }

// Xử lý hiển thị Toast cho việc download
        LaunchedEffect(downloadState) {
            when (val state = downloadState) {
                is SubmittedCVViewModel.DownloadState.Success -> {
                    Toast.makeText(context, "Đã lưu CV vào thư mục Downloads của bạn.", Toast.LENGTH_LONG).show()
                }
                is SubmittedCVViewModel.DownloadState.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
    }


