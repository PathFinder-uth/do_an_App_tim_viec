package com.example.pathfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pathfinder.data.local.SessionManager
import com.example.pathfinder.data.remote.IFirebaseUserService

class SelectUserTypeViewModelFactory(
    private val sessionManager: SessionManager,
    private val firebaseUserService: IFirebaseUserService
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SelectUserTypeViewModel::class.java)) {
            return SelectUserTypeViewModel(sessionManager, firebaseUserService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}