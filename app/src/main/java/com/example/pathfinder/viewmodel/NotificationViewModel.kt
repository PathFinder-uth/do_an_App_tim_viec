package com.example.pathfinder.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pathfinder.data.model.Notification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class NotificationViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _notifications = MutableLiveData<List<Notification>>()
    val notifications: LiveData<List<Notification>> get() = _notifications

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    // Hàm để bắt đầu lắng nghe thông báo mới
    fun listenForNotifications() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Log.w("NotificationVM", "Người dùng chưa đăng nhập, không thể lấy thông báo.")
            _notifications.value = emptyList()
            return
        }

        _isLoading.value = true

        // Lắng nghe real-time các thông báo có recipientId là của người dùng hiện tại
        // và sắp xếp theo thời gian mới nhất
        db.collection("notifications")
            .whereEqualTo("recipientId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                _isLoading.value = false

                if (error != null) {
                    Log.e("NotificationVM", "Lỗi khi lắng nghe thông báo:", error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val notificationList = snapshot.toObjects(Notification::class.java)
                    _notifications.value = notificationList
                }
            }
    }

    // Hàm để đánh dấu một thông báo là đã đọc
    fun markAsRead(notificationId: String) {
        if (notificationId.isBlank()) return

        db.collection("notifications").document(notificationId)
            .update("isRead", true)
            .addOnSuccessListener {
                Log.d("NotificationVM", "Đã đánh dấu thông báo $notificationId là đã đọc.")
            }
            .addOnFailureListener { e ->
                Log.e("NotificationVM", "Lỗi khi đánh dấu đã đọc:", e)
            }
    }
}
