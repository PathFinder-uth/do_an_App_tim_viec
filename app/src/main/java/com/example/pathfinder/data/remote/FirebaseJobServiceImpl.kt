package com.example.pathfinder.data.remote

import android.util.Log
import com.example.pathfinder.data.model.Job
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseJobServiceImpl(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : IJobService {

    private val jobsCollection = firestore.collection("jobs")

    override suspend fun createJob(job: Job): Result<Job> {
        // Hàm này giữ nguyên
        return try {
            val recruiterId = auth.currentUser?.uid ?: return Result.failure(Exception("Chưa đăng nhập"))
            val docRef = jobsCollection.document()
            val jobWithId = job.copy(id = docRef.id, recruiterId = recruiterId)
            docRef.set(jobWithId).await()
            Result.success(jobWithId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- SỬA LẠI HÀM NÀY ĐỂ DÙNG REAL-TIME LISTENER ---
    override fun getJobs(): Flow<Result<List<Job>>> = callbackFlow {
        val query = jobsCollection.orderBy("createdAt", Query.Direction.DESCENDING)

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(Result.failure(error))
                return@addSnapshotListener
            }
            if (snapshot != null) {
                // Mapping thủ công để đảm bảo an toàn
                val jobs = snapshot.documents.mapNotNull { doc ->
                    try {
                        Job(
                            id = doc.id,
                            recruiterId = doc.getString("recruiterId") ?: "",
                            title = doc.getString("title") ?: "",
                            description = doc.getString("description") ?: "",
                            requirements = doc.getString("requirements") ?: "",
                            salary = doc.getString("salary") ?: "",
                            location = doc.getString("location") ?: "",
                            type = doc.getString("type") ?: "",
                            companyName = doc.getString("companyName") ?: "",
                            logoUrl = doc.getString("logoUrl") ?: "",
                            createdAt = doc.getLong("createdAt") ?: 0L,
                            deadline = doc.getLong("deadline") ?: 0L,
                            isCvSubmitted = doc.getBoolean("isCvSubmitted") ?: false,
                            isFilled = doc.getBoolean("isFilled") ?: false,
                            jobType = doc.getString("jobType") ?: "free", // <<-- ĐỌC TRƯỜNG NÀY
                            category = doc.getString("category") ?: ""
                        )
                    } catch (e: Exception) {
                        Log.e("FirestoreMapping", "Lỗi khi chuyển đổi tài liệu job ${doc.id}", e)
                        null // Bỏ qua tài liệu này nếu có lỗi
                    }
                }
                trySend(Result.success(jobs))
            }
        }
        awaitClose { listener.remove() }
    }

    // --- SỬA LẠI HÀM NÀY ĐỂ DÙNG REAL-TIME LISTENER ---
    override fun getJobsByRecruiter(recruiterId: String): Flow<Result<List<Job>>> = callbackFlow {
        val query = jobsCollection
            .whereEqualTo("recruiterId", recruiterId)
            .orderBy("createdAt", Query.Direction.DESCENDING)

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(Result.failure(error))
                return@addSnapshotListener
            }
            if (snapshot != null) {
                // Mapping thủ công để đảm bảo an toàn
                val jobs = snapshot.documents.mapNotNull { doc ->
                    try {
                        Job(
                            id = doc.id,
                            recruiterId = doc.getString("recruiterId") ?: "",
                            title = doc.getString("title") ?: "",
                            description = doc.getString("description") ?: "",
                            requirements = doc.getString("requirements") ?: "",
                            salary = doc.getString("salary") ?: "",
                            location = doc.getString("location") ?: "",
                            type = doc.getString("type") ?: "",
                            companyName = doc.getString("companyName") ?: "",
                            logoUrl = doc.getString("logoUrl") ?: "",
                            createdAt = doc.getLong("createdAt") ?: 0L,
                            deadline = doc.getLong("deadline") ?: 0L,
                            isCvSubmitted = doc.getBoolean("isCvSubmitted") ?: false,
                            isFilled = doc.getBoolean("isFilled") ?: false,
                            jobType = doc.getString("jobType") ?: "free", // <<-- ĐỌC TRƯỜNG NÀY
                            category = doc.getString("category") ?: ""
                        )
                    } catch (e: Exception) {
                        Log.e("FirestoreMapping", "Lỗi khi chuyển đổi tài liệu job ${doc.id}", e)
                        null // Bỏ qua tài liệu này nếu có lỗi
                    }
                }
                trySend(Result.success(jobs))
            }
        }
        awaitClose { listener.remove() }
    }
    override fun getJobsByCategory(category: String): Flow<Result<List<Job>>> = callbackFlow {
        val query = jobsCollection
            .whereEqualTo("category", category)
            .orderBy("createdAt", Query.Direction.DESCENDING)

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(Result.failure(error))
                return@addSnapshotListener
            }
            if (snapshot != null) {
                // ĐÃ THÊM LẠI LOGIC MAPPING THỦ CÔNG
                val jobs = snapshot.documents.mapNotNull { doc ->
                    try {
                        Job(
                            id = doc.id,
                            recruiterId = doc.getString("recruiterId") ?: "",
                            title = doc.getString("title") ?: "",
                            description = doc.getString("description") ?: "",
                            requirements = doc.getString("requirements") ?: "",
                            salary = doc.getString("salary") ?: "",
                            location = doc.getString("location") ?: "",
                            type = doc.getString("type") ?: "",
                            companyName = doc.getString("companyName") ?: "",
                            logoUrl = doc.getString("logoUrl") ?: "",
                            createdAt = doc.getLong("createdAt") ?: 0L,
                            deadline = doc.getLong("deadline") ?: 0L,
                            isCvSubmitted = doc.getBoolean("isCvSubmitted") ?: false,
                            isFilled = doc.getBoolean("isFilled") ?: false,
                            jobType = doc.getString("jobType") ?: "free",
                            category = doc.getString("category") ?: ""
                        )
                    } catch (e: Exception) {
                        Log.e("FirestoreMapping", "Lỗi khi chuyển đổi tài liệu job ${doc.id}", e)
                        null
                    }
                }
                trySend(Result.success(jobs))
            }
        }
        awaitClose { listener.remove() }
    }

    override suspend fun getJobById(jobId: String): Result<Job> {
        // Hàm này giữ nguyên
        return try {
            val snapshot = jobsCollection.document(jobId).get().await()
            val job = snapshot.toObject(Job::class.java)
            if (job != null) Result.success(job)
            else Result.failure(Exception("Không tìm thấy công việc"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
