package com.example.pathfinder.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pathfinder.data.remote.CloudinaryManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CvUploadViewModel : ViewModel() {

    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState: StateFlow<UploadState> = _uploadState
    fun resetUploadState() {
        _uploadState.value = UploadState.Idle
    }
    fun uploadCv(fileUri: Uri, context: Context) {
        viewModelScope.launch {
            _uploadState.value = UploadState.Loading
            try {
                CloudinaryManager.init(context)
                val uploadedUrl = uploadFileToCloudinary(fileUri)
                _uploadState.value = UploadState.Success(uploadedUrl)
            } catch (e: Exception) {
                // Log lỗi chi tiết để debug dễ dàng hơn
                Log.e("CvUploadViewModel", "Error uploading CV: ${e.message}")
                _uploadState.value = UploadState.Error(e.message ?: "Tải lên thất bại")
            }
        }
    }

    private suspend fun uploadFileToCloudinary(uri: Uri): String {
        return CloudinaryManager.uploadPdf(uri)
    }

    sealed class UploadState {
        object Idle : UploadState()
        object Loading : UploadState()
        data class Success(val url: String) : UploadState()
        data class Error(val message: String) : UploadState()
    }
}