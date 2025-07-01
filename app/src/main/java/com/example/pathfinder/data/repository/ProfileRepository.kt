package com.example.pathfinder.data.repository

import android.content.Context
import android.net.Uri
import com.example.pathfinder.data.model.RecruiterProfile
import com.example.pathfinder.data.model.UserProfile
import com.example.pathfinder.data.remote.IFirebaseUserService
import com.example.pathfinder.data.remote.CloudinaryManager
import com.example.pathfinder.data.remote.IRecruiterService
import kotlinx.coroutines.tasks.await

class ProfileRepository(
    private val firebaseUserService: IFirebaseUserService,
    private val recruiterService: IRecruiterService
) {

    suspend fun getUserProfile(uid: String): Result<UserProfile> {
        return firebaseUserService.getProfile(uid)
    }

    suspend fun updateUserProfile(profile: UserProfile): Result<UserProfile> {
        return firebaseUserService.updateProfile(profile)
    }
    suspend fun getRecruiterProfile(): Result<RecruiterProfile> {
        return recruiterService.getRecruiterProfile()
    }

    suspend fun updateRecruiterProfile(profile: RecruiterProfile): Result<RecruiterProfile> {
        return recruiterService.updateRecruiterProfile(profile)
    }
    suspend fun uploadAvatar(uri: Uri, context: Context): Result<String> {
        return try {
            CloudinaryManager.init(context)
            val url = CloudinaryManager.uploadImage(uri)
            Result.success(url)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}