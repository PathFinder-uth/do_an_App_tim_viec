package com.example.pathfinder.viewmodel

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log // <<-- QUAN TRỌNG: Đảm bảo có import này
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pathfinder.data.model.SubmittedCV
import com.example.pathfinder.data.model.UserProfile
import com.example.pathfinder.data.repository.SubmittedCVRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.net.HttpURLConnection
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.pathfinder.data.model.Notification
class SubmittedCVViewModel : ViewModel() {
    private val repository = SubmittedCVRepository()
    private val db = FirebaseFirestore.getInstance()

    private val _submittedCVs = MutableLiveData<List<SubmittedCV>>()
    val submittedCVs: LiveData<List<SubmittedCV>> get() = _submittedCVs


    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _downloadState = MutableLiveData<DownloadState>(DownloadState.Idle)
    val downloadState: LiveData<DownloadState> get() = _downloadState

    private val _hasApplied = MutableStateFlow<Boolean?>(null)
    val hasApplied: StateFlow<Boolean?> = _hasApplied
    // THAY THẾ HÀM CŨ BẰNG HÀM NÀY
    private var currentJobId: String? = null
    fun checkIfAlreadyApplied(jobId: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            _hasApplied.value = false // Không thể nộp nếu chưa đăng nhập
            return
        }
        viewModelScope.launch {
            try {
                val querySnapshot = db.collection("submitted_cvs")
                    .whereEqualTo("jobId", jobId)
                    .whereEqualTo("applicantId", userId)
                    .limit(1)
                    .get()
                    .await()
                _hasApplied.value = !querySnapshot.isEmpty
            } catch (e: Exception) {
                Log.e("CheckApplied", "Lỗi khi kiểm tra trạng thái nộp đơn: ", e)
                _hasApplied.value = false // Mặc định là chưa nộp nếu có lỗi
            }
        }
    }
    fun updateCvReviewedStatus(cvId: String, isReviewed: Boolean) {
        if (cvId.isBlank()) return

        // 1. Lấy danh sách hiện tại và tìm mục cần thay đổi
        val currentList = _submittedCVs.value?.toMutableList() ?: return
        val itemIndex = currentList.indexOfFirst { it.id == cvId }
        if (itemIndex == -1) return
        val originalItem = currentList[itemIndex]

        // 2. Cập nhật giao diện ngay lập tức
        val updatedItem = originalItem.copy(isReviewed = isReviewed)
        currentList[itemIndex] = updatedItem
        _submittedCVs.value = currentList

        // 3. Gửi yêu cầu cập nhật lên server
        db.collection("submitted_cvs").document(cvId)
            .update("isReviewed", isReviewed)
            .addOnSuccessListener {
                // Thành công, không cần làm gì vì giao diện đã được cập nhật
                Log.d("SubmittedCVViewModel", "Cập nhật trạng thái CV thành công.")
            }
            .addOnFailureListener { e ->
                // 4. Nếu thất bại, quay lại trạng thái cũ trên giao diện
                Log.e("SubmittedCVViewModel", "Lỗi khi cập nhật trạng thái CV", e)
                val revertedList = _submittedCVs.value?.toMutableList() ?: return@addOnFailureListener
                val revertedIndex = revertedList.indexOfFirst { it.id == cvId }
                if (revertedIndex != -1) {
                    revertedList[revertedIndex] = originalItem // Quay lại trạng thái cũ
                    _submittedCVs.value = revertedList
                }
                // TODO: Có thể thêm Toast để báo lỗi cho người dùng
            }
    }
    fun sendNotificationToApplicant(
        applicantId: String,
        companyName: String,
        jobTitle: String
    ) {
        if (applicantId.isBlank()) {
            Log.e("Notification", "Không thể gửi thông báo: ID người nhận rỗng.")
            return
        }

        val notificationMessage = "Nhà tuyển dụng $companyName đã xem hồ sơ của bạn cho vị trí $jobTitle và có thể sẽ liên hệ sớm."

        val notification = Notification(
            recipientId = applicantId,
            senderName = companyName,
            jobTitle = jobTitle,
            message = notificationMessage
        )

        // Lưu thông báo vào một collection mới tên là "notifications"
        db.collection("notifications")
            .add(notification)
            .addOnSuccessListener {
                Log.d("Notification", "Gửi thông báo thành công tới $applicantId")
                // TODO: Có thể thêm một LiveData để báo thành công về UI
            }
            .addOnFailureListener { e ->
                Log.e("Notification", "Lỗi khi gửi thông báo", e)
            }
    }
    // --- THÊM MỚI: Hàm để reset trạng thái khi rời màn hình ---
    fun resetApplicationStatus() {
        _hasApplied.value = null
    }

    fun fetchSubmittedCVs(jobId: String) {
        _isLoading.value = true // Bắt đầu tải
        Log.d("FirestoreDebug", "Bắt đầu lấy danh sách CV cho công việc ID: $jobId")
        db.collection("submitted_cvs")
            .whereEqualTo("jobId", jobId)
            .get()
            .addOnSuccessListener { snapshot ->
                _isLoading.value = false // Tải xong
                if (snapshot.isEmpty) {
                    Log.w("FirestoreDebug", "Truy vấn thành công nhưng không tìm thấy CV nào.")
                } else {
                    Log.i("FirestoreDebug", "Tìm thấy ${snapshot.size()} CV.")
                }
                val list = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(SubmittedCV::class.java)
                }
                _submittedCVs.value = list
            }
            .addOnFailureListener { exception ->
                _isLoading.value = false // Tải xong (thất bại)
                _submittedCVs.value = emptyList() // Đảm bảo danh sách rỗng khi lỗi

                // --- PHẦN QUAN TRỌNG NHẤT ---
                // Lỗi của bạn sẽ được in ra ở đây.
                // Hãy tìm dòng này trong Logcat, nó sẽ chứa link để tạo Index.
                Log.e("FirestoreDebug", "LỖI TRUY VẤN FIRESTORE, BẠN CẦN TẠO INDEX:", exception)
            }
    }


    fun submitCvApplication(jobId: String, cvUrl: String, jobTitle: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        // ... hàm này giữ nguyên
        viewModelScope.launch {
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                onError("Người dùng chưa đăng nhập.")
                return@launch
            }
            try {
                val userProfileDoc = db.collection("users").document(user.uid).get().await()
                val userProfile = userProfileDoc.toObject(UserProfile::class.java)
                val applicantName = userProfile?.fullName ?: user.displayName ?: "Người dùng ẩn danh"
                saveCvToFirestore(jobId, user.uid, cvUrl, applicantName, jobTitle)
                onSuccess()
            } catch (e: Exception) {
                onError("Đã xảy ra lỗi: ${e.message}")
            }
        }
    }

    fun saveCvToFirestore(jobId: String, applicantId: String, cvUrl: String, applicantName: String, jobTitle: String) {
        repository.saveCvToFirestore(jobId, applicantId, cvUrl, applicantName, jobTitle)
    }

    fun downloadCV(context: Context, submittedCV: SubmittedCV) {
        viewModelScope.launch {
            val cvUrl = submittedCV.cvUrl
            if (cvUrl.isBlank()) {
                _downloadState.value = DownloadState.Error("Không có đường dẫn file CV.")
                return@launch
            }

            _downloadState.value = DownloadState.Loading

            withContext(Dispatchers.IO) {
                try {
                    val url = URL(cvUrl)
                    val connection = url.openConnection() as HttpURLConnection
                    // BẬT TÍNH NĂNG TỰ ĐỘNG THEO DÕI CHUYỂN HƯỚNG
                    connection.instanceFollowRedirects = true
                    connection.connect()

                    // KIỂM TRA MÃ PHẢN HỒI TỪ SERVER
                    val responseCode = connection.responseCode
                    if (responseCode in 200..299) { // Mã thành công (OK, Created, etc.)
                        // Tạo tên file an toàn
                        val safeApplicantName = submittedCV.applicantName.replace(Regex("[^a-zA-Z0-9]"), "")
                        val safeJobTitle = submittedCV.jobTitle.replace(Regex("[^a-zA-Z0-9]"), "")
                        val fileName = "CV_${safeJobTitle}_${safeApplicantName}.pdf"

                        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        val file = File(downloadsDir, fileName)

                        // Vòng lặp tải file
                        val inputStream = connection.inputStream
                        val outputStream = FileOutputStream(file)
                        inputStream.copyTo(outputStream)

                        // Đóng các luồng
                        outputStream.close()
                        inputStream.close()

                        // Thông báo cho hệ thống biết có file mới
                        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                        mediaScanIntent.data = Uri.fromFile(file)
                        context.sendBroadcast(mediaScanIntent)

                        withContext(Dispatchers.Main) {
                            _downloadState.value = DownloadState.Success
                        }
                    } else {
                        // Xử lý khi server trả về lỗi (404 Not Found, 403 Forbidden, etc.)
                        withContext(Dispatchers.Main) {
                            _downloadState.value = DownloadState.Error("Lỗi server: $responseCode. Không thể tải file.")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("ManualDownload", "Lỗi khi tải file thủ công: ", e)
                    withContext(Dispatchers.Main) {
                        _downloadState.value = DownloadState.Error("Tải xuống thất bại: ${e.message}")
                    }
                }
            }
        }
    }

    sealed class DownloadState {
        object Idle : DownloadState()
        object Loading : DownloadState()
        object Success : DownloadState()
        data class Error(val message: String) : DownloadState()
    }
}
