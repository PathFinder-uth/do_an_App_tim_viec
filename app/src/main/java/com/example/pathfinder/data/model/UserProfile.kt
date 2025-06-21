package com.example.pathfinder.data.model

data class UserProfile(
    val uid: String = "", // từ FirebaseAuth
    val fullName: String = "",
    val gender: String = "",
    val address: String = "",
    val phone: String = "",
    val birthday: String = "",
    val contact: String = "",
    val avatarUrl: String = "" // URL ảnh đại diện lưu trên Firebase Storage
)