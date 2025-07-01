package com.example.pathfinder.data.remote

import android.content.Context
import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object CloudinaryManager {
    private var isInitialized = false

    fun init(context: Context) {
        if (!isInitialized) {
            val config = mapOf(
                "cloud_name" to "dvf4con0g",
                "api_key" to "899335978917755",

            )
            MediaManager.init(context, config)
            isInitialized = true
        }
    }

    suspend fun uploadImage(uri: Uri): String = suspendCancellableCoroutine { continuation ->
        MediaManager.get().upload(uri)
            .unsigned("ml_default")
            .option("folder", "avatars/",)
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {}

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}

                override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                    val url = resultData?.get("secure_url") as? String
                    if (url != null) continuation.resume(url)
                    else continuation.resumeWithException(Exception("Failed to get URL"))
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    continuation.resumeWithException(Exception(error?.description))
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                    continuation.resumeWithException(Exception(error?.description))
                }
            }).dispatch()

    }
    suspend fun uploadPdf(uri: Uri): String = suspendCancellableCoroutine { continuation ->
        // SỬA LẠI ĐỂ DÙNG PRESET MỚI, CÔNG KHAI
        MediaManager.get().upload(uri)
            .unsigned("pdf_public_preset")
            .option("resource_type", "raw") // <<-- THÊM DÒNG NÀY
            .option("folder", "pdfs/")
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {}
                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}
                override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                    val url = resultData?.get("secure_url") as? String
                    if (url != null) continuation.resume(url)
                    else continuation.resumeWithException(Exception("Failed to get URL"))
                }
                override fun onError(requestId: String?, error: ErrorInfo?) {
                    continuation.resumeWithException(Exception(error?.description))
                }
                override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                    continuation.resumeWithException(Exception(error?.description))
                }
            }).dispatch()
    }
}