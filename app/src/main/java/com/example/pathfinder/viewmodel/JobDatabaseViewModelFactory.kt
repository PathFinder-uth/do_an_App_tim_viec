package com.example.pathfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pathfinder.data.dao.JobDao

class JobDatabaseViewModelFactory(private val jobDao: JobDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JobDatabaseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JobDatabaseViewModel(jobDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}