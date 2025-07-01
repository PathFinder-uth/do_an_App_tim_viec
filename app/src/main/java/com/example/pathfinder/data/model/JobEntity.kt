package com.example.pathfinder.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jobs")
data class JobEntity(
    @PrimaryKey val id: String = "",             // ID của công việc
    val recruiterId: String = "",               // UID của recruiter
    val title: String = "",
    val description: String = "",
    val requirements: String = "",
    val salary: String = "",
    val location: String = "",
    val type: String = "",                      // Ví dụ: Full-time, Part-time, Remote
    val companyName: String = "",
    val logoUrl: String = "",                   // URL của logo công ty
    val createdAt: Long = System.currentTimeMillis(),
    val deadline: Long = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000,
    val cvUrl: String = "",
    val isCvSubmitted: Boolean = false
)