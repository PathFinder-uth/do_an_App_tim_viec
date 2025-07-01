package com.example.pathfinder.ui.screen.job

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.pathfinder.data.model.Job
import com.example.pathfinder.data.model.JobEntity
import com.example.pathfinder.di.AppContainer
import com.example.pathfinder.navigation.Screen
import com.example.pathfinder.viewmodel.JobDatabaseViewModel
import com.example.pathfinder.viewmodel.JobDatabaseViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedJobsScreen(navController: NavHostController) {
    val jobDao = AppContainer.jobDao // Lấy JobDao từ AppContainer

    // Khởi tạo ViewModel với ViewModelFactory
    val jobDatabaseViewModel: JobDatabaseViewModel = viewModel(
        factory = JobDatabaseViewModelFactory(jobDao)
    )

    // Quan sát LiveData trong ViewModel
    val savedJobs by jobDatabaseViewModel.savedJobs.observeAsState(emptyList())
    val selectedJobs = remember { mutableStateListOf<JobEntity>() }

    // Handle deletion logic
    val handleDeleteJobs = {
        // Delete selected jobs
        selectedJobs.forEach { job ->
            jobDatabaseViewModel.deleteJob(job)
        }
        selectedJobs.clear() // Clear selection after deletion
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Danh sách công việc đã lưu") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF67C2F6))
            )
        },
        floatingActionButton = {
            // Delete button that appears when jobs are selected
            if (selectedJobs.isNotEmpty()) {
                FloatingActionButton(
                    onClick = handleDeleteJobs,
                    containerColor = Color.Red  // Use containerColor instead of backgroundColor
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Selected Jobs")
                }
            }
        }
    ) { padding ->
        if (savedJobs.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Chưa có công việc nào được lưu.")
            }
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
                        // Checkbox to select job for deletion
                        Checkbox(
                            checked = selectedJobs.contains(jobEntity),
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
                                    selectedJobs.add(jobEntity)
                                } else {
                                    selectedJobs.remove(jobEntity)
                                }
                            }
                        )
                        JobCard(job = job, onClick = {
                            navController.navigate(Screen.JobDetail.withArgs(job.id))
                        })
                    }
                }
            }
        }
    }
}