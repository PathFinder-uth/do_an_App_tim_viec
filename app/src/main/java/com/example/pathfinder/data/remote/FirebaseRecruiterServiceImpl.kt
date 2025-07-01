package com.example.pathfinder.data.remote

import com.example.pathfinder.data.model.RecruiterProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseRecruiterServiceImpl(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : IRecruiterService {

    override suspend fun getRecruiterProfile(): Result<RecruiterProfile> {
        return try {
            val uid = auth.currentUser?.uid ?: return Result.failure(Exception("Chưa đăng nhập"))
            val snapshot = firestore.collection("recruiters").document(uid).get().await()
            val profile = snapshot.toObject(RecruiterProfile::class.java)?.copy(uid = uid)
            if (profile != null) Result.success(profile)
            else Result.failure(Exception("Không tìm thấy recruiter"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateRecruiterProfile(profile: RecruiterProfile): Result<RecruiterProfile> {
        return try {
            val uid = auth.currentUser?.uid ?: return Result.failure(Exception("Chưa đăng nhập"))
            val updated = profile.copy(uid = uid)
            firestore.collection("recruiters").document(uid).set(updated).await()
            Result.success(updated)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}