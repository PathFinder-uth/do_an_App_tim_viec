package com.example.pathfinder.data.remote

import com.example.pathfinder.data.model.UserProfile

interface IFirebaseUserService {
    suspend fun getProfile(): Result<UserProfile>

    suspend fun updateProfile(profile: UserProfile): Result<UserProfile>
}