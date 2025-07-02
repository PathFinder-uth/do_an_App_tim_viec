package com.example.pathfinder.data.remote

import com.example.pathfinder.data.model.Job
import kotlinx.coroutines.flow.Flow

interface IJobService {
    suspend fun createJob(job: Job): Result<Job>
    // Thay đổi: Trả về một dòng chảy (Flow) dữ liệu thay vì một kết quả duy nhất
    fun getJobs(): Flow<Result<List<Job>>>
    fun getJobsByRecruiter(recruiterId: String): Flow<Result<List<Job>>>
    suspend fun getJobById(jobId: String): Result<Job>
    fun getJobsByCategory(category: String): Flow<Result<List<Job>>>
    suspend fun deleteJob(jobId: String): Result<Unit>
    suspend fun updateJob(job: Job): Result<Unit>
}
