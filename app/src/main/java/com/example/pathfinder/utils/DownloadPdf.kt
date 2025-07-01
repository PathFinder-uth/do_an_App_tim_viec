package com.example.pathfinder.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast

fun downloadPdf(context: Context, fileUrl: String, fileName: String) {
    val request = DownloadManager.Request(Uri.parse(fileUrl))
        .setTitle("Tải CV")
        .setDescription("Đang tải $fileName")
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        .setAllowedOverMetered(true)
        .setAllowedOverRoaming(true)

    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    downloadManager.enqueue(request)

    Toast.makeText(context, "Đang tải xuống CV...", Toast.LENGTH_SHORT).show()
}