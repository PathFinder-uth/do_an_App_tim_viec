package com.example.pathfinder.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pathfinder.data.model.Job
import com.example.pathfinder.data.repository.JobRepository
import com.example.pathfinder.di.AppContainer.algoliaSearchService
import com.example.pathfinder.viewmodel.state.JobUiState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch



class JobViewModel(
    private val jobRepository: JobRepository
) : ViewModel() {
    private var currentRecruiterId: String? = null
    private val _uiState = MutableStateFlow(JobUiState())
    val uiState: StateFlow<JobUiState> = _uiState.asStateFlow()
    private val db = FirebaseFirestore.getInstance()
    fun createJob(job: Job) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, isSuccess = false)
            val result = jobRepository.createJob(job)
            // Listener sẽ tự động cập nhật danh sách, chúng ta chỉ cần xử lý trạng thái tạo job
            _uiState.value = when {
                result.isSuccess -> _uiState.value.copy(isLoading = false, isSuccess = true)
                else -> _uiState.value.copy(isLoading = false, error = result.exceptionOrNull()?.message)
            }
        }
    }

    fun fetchAllJobs() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, jobs = emptyList())
            jobRepository.getAllJobs().collect { result ->
                val now = System.currentTimeMillis()
                if (result.isSuccess) {
                    val filteredJobs = result.getOrDefault(emptyList()).filter { it.deadline > now }
                    _uiState.value = _uiState.value.copy(jobs = filteredJobs, isLoading = false)
                } else {
                    _uiState.value = _uiState.value.copy(error = result.exceptionOrNull()?.message, isLoading = false)
                }
            }
        }
    }

    fun filterJobsByCategory(category: String) {
        viewModelScope.launch {
            // Xóa danh sách cũ và hiển thị loading ngay lập tức
            _uiState.value = _uiState.value.copy(isLoading = true, jobs = emptyList())
            jobRepository.getJobsByCategory(category).collect { result ->
                val now = System.currentTimeMillis()
                if (result.isSuccess) {
                    val filteredJobs = result.getOrDefault(emptyList()).filter { it.deadline > now }
                    _uiState.value = _uiState.value.copy(jobs = filteredJobs, isLoading = false)
                } else {
                    _uiState.value = _uiState.value.copy(error = result.exceptionOrNull()?.message, isLoading = false)
                }
            }
        }
    }

    fun fetchJobsByRecruiter(recruiterId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, jobs = emptyList())
            jobRepository.getJobsByRecruiter(recruiterId).collect { result ->
                val now = System.currentTimeMillis()
                if (result.isSuccess) {
                    val filteredJobs = result.getOrDefault(emptyList()).filter { it.deadline > now }
                    _uiState.value = _uiState.value.copy(jobs = filteredJobs, isLoading = false)
                } else {
                    _uiState.value = _uiState.value.copy(error = result.exceptionOrNull()?.message, isLoading = false)
                }
            }
        }
    }
    fun updateJobFilledStatus(jobId: String, isFilled: Boolean) {
        if (jobId.isBlank()) return

        // 1. Lấy danh sách hiện tại và tìm mục cần thay đổi
        val currentJobs = _uiState.value.jobs.toMutableList()
        val jobIndex = currentJobs.indexOfFirst { it.id == jobId }
        if (jobIndex == -1) return
        val originalJob = currentJobs[jobIndex]

        // 2. Cập nhật giao diện ngay lập tức
        val updatedJob = originalJob.copy(isFilled = isFilled)
        currentJobs[jobIndex] = updatedJob
        _uiState.value = _uiState.value.copy(jobs = currentJobs)

        // 3. Gửi yêu cầu cập nhật lên server
        db.collection("jobs").document(jobId)
            .update("isFilled", isFilled)
            .addOnSuccessListener {
                // Thành công, không cần làm gì
                Log.d("JobViewModel", "Cập nhật trạng thái job thành công.")
            }
            .addOnFailureListener { e ->
                // 4. Nếu thất bại, quay lại trạng thái cũ trên giao diện
                Log.e("JobViewModel", "Lỗi khi cập nhật trạng thái job", e)
                val revertedJobs = _uiState.value.jobs.toMutableList()
                val revertedIndex = revertedJobs.indexOfFirst { it.id == jobId }
                if (revertedIndex != -1) {
                    revertedJobs[revertedIndex] = originalJob
                    _uiState.value = _uiState.value.copy(jobs = revertedJobs)
                }
            }
    }
    fun getJobDetail(jobId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = jobRepository.getJobById(jobId)
            _uiState.value = if (result.isSuccess) {
                _uiState.value.copy(selectedJob = result.getOrNull(), isLoading = false)
            } else {
                _uiState.value.copy(error = result.exceptionOrNull()?.message, isLoading = false)
            }
        }
    }
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

    fun deleteJobs(jobIds: List<String>) {
        viewModelScope.launch {
            jobIds.forEach { jobId ->
                jobRepository.deleteJob(jobId)
            }
            // Sau khi xóa, không cần làm gì thêm vì Real-time listener sẽ tự động cập nhật danh sách
        }
    }

    fun updateJob(job: Job) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, isSuccess = false)
            val result = jobRepository.updateJob(job)
            _uiState.value = when {
                result.isSuccess -> _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true, // Đặt cờ thành công để điều hướng
                    error = null
                )
                else -> _uiState.value.copy(
                    isLoading = false,
                    isSuccess = false,
                    error = result.exceptionOrNull()?.message
                )
            }
        }
    }
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearSuccessFlag() {
        _uiState.value = _uiState.value.copy(isSuccess = false)
    }
}