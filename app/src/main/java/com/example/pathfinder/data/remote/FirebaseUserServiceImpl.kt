package com.example.pathfinder.data.remote

import com.example.pathfinder.data.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseUserServiceImpl(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : IFirebaseUserService {

    override suspend fun getProfile(): Result<UserProfile> {
        return try {
            val uid = auth.currentUser?.uid ?: return Result.failure(Exception("Chưa đăng nhập"))
            val snapshot = firestore.collection("users").document(uid).get().await()
            val profile = snapshot.toObject(UserProfile::class.java)?.copy(uid = uid)
            if (profile != null) {
                Result.success(profile)
            } else {
                Result.failure(Exception("Không tìm thấy dữ liệu người dùng"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(profile: UserProfile): Result<UserProfile> {
        return try {
            val uid = auth.currentUser?.uid ?: return Result.failure(Exception("Chưa đăng nhập"))
            val updatedProfile = profile.copy(uid = uid) // 🔒 Gán UID chuẩn luôn
            firestore.collection("users").document(uid).set(updatedProfile).await()
            Result.success(updatedProfile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}