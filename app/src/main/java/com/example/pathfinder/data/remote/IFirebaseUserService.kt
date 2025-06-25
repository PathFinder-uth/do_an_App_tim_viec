package com.example.pathfinder.data.remote

import com.example.pathfinder.data.model.RecruiterProfile
import com.example.pathfinder.data.model.UserProfile

interface IFirebaseUserService {
    suspend fun getProfile(uid: String): Result<UserProfile>

    suspend fun updateProfile(profile: UserProfile): Result<UserProfile>// 🔄 sửa thành
    suspend fun updateUserRole(uid: String, role: String): Result<Unit>
}