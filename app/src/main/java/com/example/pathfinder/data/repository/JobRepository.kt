package com.example.pathfinder.data.repository

import com.example.pathfinder.data.model.Job
import com.example.pathfinder.data.remote.IJobService
import kotlinx.coroutines.flow.Flow

class JobRepository(
    private val jobService: IJobService
) {
    suspend fun createJob(job: Job): Result<Job> {
        return jobService.createJob(job)
    }

    // Thay đổi: Trả về Flow
    fun getAllJobs(): Flow<Result<List<Job>>> {
        return jobService.getJobs()
    }

    // Thay đổi: Trả về Flow
    fun getJobsByRecruiter(recruiterId: String): Flow<Result<List<Job>>> {
        return jobService.getJobsByRecruiter(recruiterId)
    }
    fun getJobsByCategory(category: String): Flow<Result<List<Job>>> {
        return jobService.getJobsByCategory(category)
    }
    suspend fun getJobById(jobId: String): Result<Job> {
        return jobService.getJobById(jobId)
    }
    suspend fun deleteJob(jobId: String): Result<Unit> {
        return jobService.deleteJob(jobId)
    }

    suspend fun updateJob(job: Job): Result<Unit> {
        return jobService.updateJob(job)
    }
}
