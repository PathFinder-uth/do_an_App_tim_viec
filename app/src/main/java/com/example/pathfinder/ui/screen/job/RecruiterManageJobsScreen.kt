package com.example.pathfinder.ui.screen.job

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun RecruiterManageJobsScreen(
    navController: NavController,
    jobViewModel: JobViewModel = viewModel(factory = AppContainer.jobViewModelFactory)
) {
    val uiState by jobViewModel.uiState.collectAsState()
    val postedJobs = uiState.jobs
    val isLoading = uiState.isLoading
    val context = LocalContext.current

    var isInDeleteMode by remember { mutableStateOf(false) }
    val selectedJobsForDeletion = remember { mutableStateListOf<String>() }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val recruiterId = FirebaseAuth.getInstance().currentUser?.uid
        if (recruiterId != null) {
            jobViewModel.fetchJobsByRecruiter(recruiterId)
        }
    }

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Xác nhận xóa") },
            text = { Text("Bạn có chắc chắn muốn xóa ${selectedJobsForDeletion.size} công việc đã chọn không?") },
            confirmButton = {
                TextButton(onClick = {
                    jobViewModel.deleteJobs(selectedJobsForDeletion.toList())
                    showDeleteConfirmDialog = false
                    isInDeleteMode = false
                    selectedJobsForDeletion.clear()
                    Toast.makeText(context, "Đã xóa!", Toast.LENGTH_SHORT).show()
                }) { Text("Đồng ý") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) { Text("Hủy") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quản lý bài đăng") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                actions = {
                    if (isInDeleteMode && selectedJobsForDeletion.isNotEmpty()) {
                        IconButton(onClick = { showDeleteConfirmDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Xóa mục đã chọn", tint = Color.Red)
                        }
                    }
                    IconButton(onClick = { isInDeleteMode = !isInDeleteMode }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Chế độ xóa",
                            tint = if (isInDeleteMode) MaterialTheme.colorScheme.primary else Color.Gray
                        )
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
                        ManageableJobItem(
                            job = job,
                            isInDeleteMode = isInDeleteMode,
                            isSelected = job.id in selectedJobsForDeletion,
                            onJobClick = {
                                // Điều hướng đến màn hình chỉnh sửa
                                navController.navigate(Screen.JobForm.withEditArgs(job.id))
                            },
                            onSelectToggle = {
                                if (job.id in selectedJobsForDeletion) {
                                    selectedJobsForDeletion.remove(job.id)
                                } else {
                                    selectedJobsForDeletion.add(job.id)
                                }
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
private fun ManageableJobItem(
    job: Job,
    isInDeleteMode: Boolean,
    isSelected: Boolean,
    onJobClick: () -> Unit,
    onSelectToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                // Nếu đang ở chế độ xóa, nhấn vào sẽ là chọn. Nếu không, sẽ là điều hướng.
                onClick = { if (isInDeleteMode) onSelectToggle() else onJobClick() }
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isInDeleteMode) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onSelectToggle() }
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = job.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = job.companyName, style = MaterialTheme.typography.bodyMedium)
        }
        // Nút chỉnh sửa chỉ hiện khi không ở chế độ xóa
        if (!isInDeleteMode) {
            IconButton(onClick = onJobClick) {
                Icon(Icons.Default.Edit, contentDescription = "Chỉnh sửa")
            }
        }
    }
}
