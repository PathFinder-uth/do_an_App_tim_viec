package com.example.pathfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pathfinder.data.remote.AlgoliaSearchService

class JobSearchViewModelFactory(
    private val algoliaSearchService: AlgoliaSearchService
) : ViewModelProvider.Factory { // Kế thừa đúng ViewModelProvider.Factory

    // Ghi đè phương thức create từ ViewModelProvider.Factory
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Kiểm tra xem lớp ViewModel có phải là JobSearchViewModel không
        if (modelClass.isAssignableFrom(JobSearchViewModel::class.java)) {
            return JobSearchViewModel(algoliaSearchService) as T // Tạo JobSearchViewModel
        }
        // Nếu không phải, ném ra lỗi
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}