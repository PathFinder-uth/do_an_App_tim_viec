package com.example.pathfinder.ui.screen.job

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.pathfinder.data.model.Job
import com.example.pathfinder.data.model.JobEntity
import com.example.pathfinder.navigation.Screen
import com.example.pathfinder.viewmodel.JobDatabaseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmittedJobsScreen(navController: NavHostController,
                        onJobClicked: (String) -> Unit // Callback when a job is clicked (to show details)
) {
    // Lấy JobDatabaseViewModel
    val jobDatabaseViewModel: JobDatabaseViewModel = viewModel()

    // Quan sát danh sách công việc đã nộp
    val savedJobs by jobDatabaseViewModel.savedJobs.observeAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Đơn đã nộp") },
                navigationIcon = {
                    IconButton(onClick = { /* Hành động quay lại */ }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (savedJobs.isEmpty()) {
                // Hiển thị thông báo nếu không có công việc nào
                Text(
                    text = "Chưa có đơn nào được nộp",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    items(savedJobs) { jobEntity ->
                        val job = Job(
                            id = jobEntity.id,
                            recruiterId = jobEntity.recruiterId,
                            title = jobEntity.title,
                            description = jobEntity.description,
                            requirements = jobEntity.requirements,
                            salary = jobEntity.salary,
                            location = jobEntity.location,
                            type = jobEntity.type,
                            companyName = jobEntity.companyName,
                            logoUrl = jobEntity.logoUrl,
                            createdAt = jobEntity.createdAt,
                            deadline = jobEntity.deadline,

                            )
                        // Row for job with checkbox and delete option
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            JobCard(job = job, onClick = {
                                navController.navigate(Screen.JobDetail.withArgs(job.id))
                            })
                        }
                    }
                }
            }
        }
    }
}