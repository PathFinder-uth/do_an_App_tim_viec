package com.example.pathfinder.viewmodel.state

data class SettingsUiState(
    val pushNotificationsEnabled: Boolean = true,
    val emailNotificationsEnabled: Boolean = true,
    val darkModePreference: String = "system",
    val isLoading: Boolean = false,
    val message: String? = null
)