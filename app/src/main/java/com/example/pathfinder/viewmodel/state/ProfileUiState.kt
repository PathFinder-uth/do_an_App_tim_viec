package com.example.pathfinder.viewmodel.state

import android.net.Uri

data class ProfileUiState(
    val fullName: String = "",
    val gender: String = "",
    val address: String = "",
    val phone: String = "",
    val birthday: String = "",
    val contact: String = "",
    val avatarUrl: String = "",
    val tempAvatarUri: Uri? = null,
    val message: String = ""
)
