package com.example.pathfinder.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.pathfinder.MainActivity // Thay thế bằng Activity chính của bạn
import com.example.pathfinder.R // Bạn có thể cần tạo file icon trong res/drawable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    // Được gọi khi có một tin nhắn mới từ FCM
    // Được gọi khi có một tin nhắn mới từ FCM
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM_SERVICE", "From: ${remoteMessage.from}")
        remoteMessage.notification?.let {
            Log.d("FCM_SERVICE", "Message Notification Body: ${it.body}")
            sendNotification(it.title, it.body)
        }
    }

    // Được gọi khi một token mới được tạo ra cho thiết bị
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM_SERVICE", "Refreshed token: $token")
        // Gửi token mới này lên server của bạn
        sendRegistrationToServer(token)
    }

    companion object {
        // --- HÀM MỚI ĐỂ CẬP NHẬT TOKEN MỘT CÁCH CHỦ ĐỘNG ---
        // Bạn có thể gọi hàm này sau khi người dùng đăng nhập thành công
        fun updateFCMToken() {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FCM_TOKEN_UPDATE", "Fetching FCM registration token failed", task.exception)
                    return@addOnCompleteListener
                }
                // Lấy token mới và gửi lên server
                val token = task.result
                Log.d("FCM_TOKEN_UPDATE", "Current token: $token")
                sendRegistrationToServer(token)
            }
        }

        private fun sendRegistrationToServer(token: String?) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null && token != null) {
                val userRef = FirebaseFirestore.getInstance().collection("users").document(userId)
                userRef.update("fcmToken", token)
                    .addOnSuccessListener { Log.d("FCM_TOKEN_UPDATE", "FCM Token updated successfully for user $userId") }
                    .addOnFailureListener { e -> Log.w("FCM_TOKEN_UPDATE", "Error updating FCM token", e) }
            } else {
                Log.d("FCM_TOKEN_UPDATE", "Cannot update token: User not logged in or token is null.")
            }
        }
    }

    private fun sendNotification(title: String?, messageBody: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        val channelId = "default_channel_id" // ID của kênh thông báo
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification_icon) // <<-- TẠO ICON NÀY
            .setContentTitle(title ?: "Thông báo mới")
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Đối với Android 8.0 (Oreo) trở lên, cần phải có Notification Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Thông báo chung",
                NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }
}
