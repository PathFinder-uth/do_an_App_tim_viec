package com.example.pathfinder.ui.screen.cv

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pathfinder.viewmodel.CvUploadViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadCvScreen(
    onCancel: () -> Unit // Chỉ cần callback để hủy
) {
    val context = LocalContext.current
    // Lấy viewModel được chia sẻ từ màn hình cha
    val viewModel: CvUploadViewModel = viewModel()
    val uploadState by viewModel.uploadState.collectAsState()

    var selectedPdfUri by remember { mutableStateOf<Uri?>(null) }
    var fileName by remember { mutableStateOf("") }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                selectedPdfUri = uri
                fileName = uri.lastPathSegment ?: "Không xác định"
            }
        }
    )

    // Xử lý hiển thị Toast báo lỗi tại đây
    LaunchedEffect(uploadState) {
        if (uploadState is CvUploadViewModel.UploadState.Error) {
            Toast.makeText(context, (uploadState as CvUploadViewModel.UploadState.Error).message, Toast.LENGTH_LONG).show()
            viewModel.resetUploadState() // Reset để cho phép thử lại
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nộp CV") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = { filePickerLauncher.launch("application/pdf") }) {
                Text("Chọn file PDF")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (fileName.isNotEmpty()) {
                Text("Đã chọn: $fileName")
            }

            Spacer(modifier = Modifier.height(32.dp))

            when {
                uploadState is CvUploadViewModel.UploadState.Loading -> {
                    CircularProgressIndicator()
                }
                selectedPdfUri != null -> {
                    Button(
                        onClick = {
                            selectedPdfUri?.let { uri ->
                                viewModel.uploadCv(uri, context)
                            }
                        },
                        enabled = uploadState !is CvUploadViewModel.UploadState.Loading
                    ) {
                        Text("Nộp CV")
                    }
                }
            }
        }
    }
}