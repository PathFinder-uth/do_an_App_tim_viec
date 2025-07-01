package com.example.pathfinder.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Notification(
    @DocumentId val id: String = "",
    val recipientId: String = "", // ID của người nhận (ứng viên)
    val senderName: String = "",  // Tên người gửi (công ty của nhà tuyển dụng)
    val jobTitle: String = "",    // Công việc liên quan
    val message: String = "",     // Nội dung thông báo
    @ServerTimestamp val timestamp: Date? = null, // Thời gian gửi
    val isRead: Boolean = false
)
