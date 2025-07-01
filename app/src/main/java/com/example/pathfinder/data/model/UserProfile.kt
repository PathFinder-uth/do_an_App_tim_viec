package com.example.pathfinder.data.model

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@IgnoreExtraProperties
data class UserProfile(
    // Giữ nguyên các trường cũ
    @get:PropertyName("role") @set:PropertyName("role")
    var role: String? = null,

    @get:PropertyName("uid") @set:PropertyName("uid")
    var uid: String = "",

    @get:PropertyName("fullName") @set:PropertyName("fullName")
    var fullName: String = "",

    @get:PropertyName("gender") @set:PropertyName("gender")
    var gender: String = "",

    @get:PropertyName("address") @set:PropertyName("address")
    var address: String = "",

    @get:PropertyName("phone") @set:PropertyName("phone")
    var phone: String = "",

    @get:PropertyName("birthday") @set:PropertyName("birthday")
    var birthday: String = "",

    @get:PropertyName("contact") @set:PropertyName("contact")
    var contact: String = "",

    @get:PropertyName("avatarUrl") @set:PropertyName("avatarUrl")
    var avatarUrl: String = "",

    // --- THÊM TRƯỜNG MỚI NÀY VÀO ---
    // Để lưu token cho push notification
    @get:PropertyName("fcmToken") @set:PropertyName("fcmToken")
    var fcmToken: String = "",

    @get:PropertyName("isPremium") @set:PropertyName("isPremium")
    var isPremium: Boolean = false // Mặc định là người dùng thường

)
