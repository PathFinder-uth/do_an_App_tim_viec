package com.example.pathfinder.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pathfinder.data.model.JobEntity

@Dao
interface JobDao {
    // Thêm công việc vào database, nếu trùng lặp thì thay thế
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(job: JobEntity)

    // Lấy tất cả công việc đã lưu
    @Query("SELECT * FROM jobs")
    suspend fun getAllJobs(): List<JobEntity>

    // Lấy công việc theo ID
    @Query("SELECT * FROM jobs WHERE id = :jobId")
    suspend fun getJobById(jobId: String): JobEntity?

    @Query("SELECT * FROM jobs WHERE cvUrl IS NOT NULL AND cvUrl != ''")
    suspend fun getJobsWithCv(): List<JobEntity> // Lấy công việc đã nộp CV
    @Delete
    suspend fun deleteJob(job: JobEntity)
}