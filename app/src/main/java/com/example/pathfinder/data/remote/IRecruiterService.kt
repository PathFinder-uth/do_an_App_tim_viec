package com.example.pathfinder.data.remote

import com.example.pathfinder.data.model.RecruiterProfile

interface IRecruiterService {
    suspend fun getRecruiterProfile(): Result<RecruiterProfile>
    suspend fun updateRecruiterProfile(profile: RecruiterProfile): Result<RecruiterProfile>
}