package com.example.pathfinder.data.model

data class RecruiterProfile(
    val uid: String = "",
    val companyName: String = "",
    val description: String = "",
    val address: String = "",
    val phone: String = "",
    val email: String = "",
    val website: String = "",
    val industry: String = "",
    val companySize: String = "",
    val logoUrl: String = "" ,// nếu có hỗ trợ upload
    val role: String? = null
)