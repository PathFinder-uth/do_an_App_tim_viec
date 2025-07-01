package com.example.pathfinder.viewmodel.state

import com.example.pathfinder.data.model.Job

data class JobUiState(
    val isLoading: Boolean = false,
    val jobs: List<Job> = emptyList(),
    val selectedJob: Job? = null,
    val isSuccess: Boolean = false,
    val error: String? = null
)