package com.example.pathfinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pathfinder.data.dao.JobDao
import com.example.pathfinder.data.model.JobEntity
import kotlinx.coroutines.launch

class JobDatabaseViewModel(private val jobDao: JobDao) : ViewModel() {

    // LiveData để quan sát danh sách công việc đã lưu
    private val _savedJobs = MutableLiveData<List<JobEntity>>(emptyList())
    val savedJobs: LiveData<List<JobEntity>> get() = _savedJobs

    init {
        loadSavedJobs() // Gọi loadSavedJobs() khi ViewModel được tạo
    }

    // Lưu công việc vào Room Database
    fun saveJob(job: JobEntity) {
        viewModelScope.launch {
            val currentList = _savedJobs.value ?: emptyList()
            // Kiểm tra xem công việc đã tồn tại trong danh sách chưa
            if (currentList.none { it.id == job.id }) {
                jobDao.insertJob(job)  // Lưu công việc mới
                loadSavedJobs()  // Sau khi lưu xong, tải lại danh sách công việc đã lưu
                println("Job saved successfully: $job")
            } else {
                println("Job already exists in saved jobs.")
            }
        }
    }

    // Lấy tất cả công việc đã lưu từ cơ sở dữ liệu
    fun loadSavedJobs() {
        viewModelScope.launch {
            val jobs = jobDao.getAllJobs()  // Lấy danh sách công việc từ Room Database
            _savedJobs.postValue(jobs)  // Cập nhật LiveData
        }
    }
    fun saveJobWithCvUrl(jobId: String, cvUrl: String) {
        viewModelScope.launch {
            val job = jobDao.getJobById(jobId)  // Lấy công việc từ database theo ID
            job?.let {
                // Cập nhật công việc với URL của CV và đánh dấu đã nộp CV
                val updatedJob = it.copy(cvUrl = cvUrl, isCvSubmitted = true)
                jobDao.insertJob(updatedJob)  // Lưu công việc đã cập nhật vào cơ sở dữ liệu
            }
        }
    }

    // Lọc các công việc chưa hết hạn và cập nhật danh sách
    fun fetchAllJobs() {
        viewModelScope.launch {
            val result = jobDao.getAllJobs()  // Lấy tất cả công việc
            val now = System.currentTimeMillis()  // Lấy thời gian hiện tại
            // Lọc các công việc chưa hết hạn (deadline > thời gian hiện tại)
            val filteredJobs = result.filter { it.deadline > now }
            _savedJobs.postValue(filteredJobs)  // Cập nhật LiveData với công việc đã lọc
        }
    }
    fun fetchJobsWithCv() {
        viewModelScope.launch {
            val jobsWithCv = jobDao.getJobsWithCv()  // Lấy các công việc đã nộp CV
            _savedJobs.postValue(jobsWithCv)
        }
    }
    fun deleteJob(job: JobEntity) {
        viewModelScope.launch {
            jobDao.deleteJob(job)  // Xóa công việc khỏi cơ sở dữ liệu
            loadSavedJobs()  // Sau khi xóa xong, tải lại danh sách công việc đã lưu
            println("Job deleted successfully: $job")
        }
    }
}