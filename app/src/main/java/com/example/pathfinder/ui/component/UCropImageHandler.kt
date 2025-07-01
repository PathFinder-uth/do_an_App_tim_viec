package com.example.pathfinder.ui.component

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.content.FileProvider
import com.yalantis.ucrop.UCrop
import java.io.File
import java.util.*

@Composable
fun rememberUCropLauncher(
    context: Context,
    onCropSuccess: (Uri) -> Unit
): (Uri) -> Unit {
    val launcher = rememberLauncherForActivityResult(StartActivityForResult()) { result ->
        val data = result.data
        val uri = data?.let { UCrop.getOutput(it) }
        if (result.resultCode == android.app.Activity.RESULT_OK && uri != null) {
            onCropSuccess(uri)
        }
    }

    return remember {
        { sourceUri: Uri ->
            val ucropDir = File(context.externalCacheDir, "ucrop")
            if (!ucropDir.exists()) ucropDir.mkdirs() // BẮT BUỘC

            val tempFile = File(ucropDir, "cropped_${UUID.randomUUID()}.jpg")
            tempFile.createNewFile() // ⛳ Bắt buộc để UCrop không bị lỗi ENOENT
            val destinationUri = Uri.fromFile(tempFile) // 👈 KHÔNG dùng FileProvider ở đây


            // Cấu hình UCrop
            val options = UCrop.Options().apply {
                setCircleDimmedLayer(true)
                setShowCropGrid(false)
                setFreeStyleCropEnabled(false)
                setCompressionQuality(90)
                setHideBottomControls(true)
            }

            val intent = UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(1f, 1f)
                .withMaxResultSize(512, 512)
                .withOptions(options)
                .getIntent(context)

            launcher.launch(intent)
        }
    }
}