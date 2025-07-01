package com.example.pathfinder.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pathfinder.data.model.Job
import com.example.pathfinder.data.remote.AlgoliaSearchService
import kotlinx.coroutines.launch

class JobSearchViewModel(
    private val algoliaSearchService: AlgoliaSearchService // Nhận AlgoliaSearchService từ factory
) : ViewModel() {

    // Mutable state để chứa kết quả tìm kiếm
    var jobResults = mutableStateListOf<Job>()
        private set

    fun searchJobs(query: String) {
        viewModelScope.launch {
            algoliaSearchService.searchJobs(query, onResult = { jobs ->
                jobResults.clear()
                jobResults.addAll(jobs)
            }, onError = { error ->
                // Xử lý lỗi nếu có
                println("Lỗi tìm kiếm: ${error.message}")
            })
        }
    }
}