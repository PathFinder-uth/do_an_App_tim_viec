package com.example.pathfinder.ui.component

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.pathfinder.navigation.Screen
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@Composable
fun DrawerContent(
    fullName: String,
    avatarUrl: String,
    navController: NavController,
    onLogout: () -> Unit
) {
    val context = LocalContext.current

    // Cho phép chọn file PDF
    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            savePdfToDownloads(context, it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color(0xFFF9F2FF))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = rememberAsyncImagePainter(avatarUrl),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = fullName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        val items = listOf(
            "Thông tin tài khoản" to Icons.Default.Person,
            "Tải CV" to Icons.Default.Create,
            "Thông báo tuyển dụng" to Icons.Default.Notifications,
            "Đơn đã nộp" to Icons.Default.CheckCircle,
            "Danh sách lưu" to Icons.Default.Star,
            "Mua gói" to Icons.Default.ShoppingCart,
            "Hỗ trợ" to Icons.Default.Help,
            "Cài đặt" to Icons.Default.Settings,
            "Đăng xuất" to Icons.Default.Logout,
        )

        items.forEach { (label, icon) ->
            Button(
                onClick = {
                    when (label) {
                        "Tải CV" -> pdfPickerLauncher.launch("application/pdf")
                        "Thông tin tài khoản" -> navController.navigate(Screen.Profile.route)
                        "Cài đặt" -> navController.navigate(Screen.Settings.route)
                        "Đăng xuất" -> onLogout()
                        // Thêm điều hướng khác nếu cần
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    disabledContainerColor = Color.LightGray
                ),
                shape = RoundedCornerShape(6.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Icon(imageVector = icon, contentDescription = label, tint = Color.Black)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = label, color = Color.Black)
            }
        }
    }
}

fun savePdfToDownloads(context: Context, uri: Uri) {
    val contentResolver = context.contentResolver
    val fileName = getFileNameFromUri(context, uri) ?: "CV_PathFinder.pdf"

    val inputStream: InputStream? = contentResolver.openInputStream(uri)
    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val outputFile = File(downloadsDir, fileName)

    try {
        val outputStream = FileOutputStream(outputFile)
        inputStream?.copyTo(outputStream)
        outputStream.close()
        inputStream?.close()

        Toast.makeText(context, "Đã lưu CV vào: ${outputFile.absolutePath}", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Lỗi khi lưu file: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

fun getFileNameFromUri(context: Context, uri: Uri): String? {
    var result: String? = null
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1 && it.moveToFirst()) {
                result = it.getString(nameIndex)
            }
        }
    }

    if (result == null) {
        result = uri.path
        val cut = result?.lastIndexOf('/') ?: -1
        if (cut != -1) {
            result = result?.substring(cut + 1)
        }
    }

    return result
}
