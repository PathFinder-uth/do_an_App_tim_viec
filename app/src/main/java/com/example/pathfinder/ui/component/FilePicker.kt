package com.example.pathfinder.ui.component

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext


@Composable
fun FilePicker(onFilePicked: (Uri) -> Unit) {
    val context = LocalContext.current
    val activity = context as? Activity
    val registryOwner = LocalActivityResultRegistryOwner.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            onFilePicked(it)
        }
    }

    Button(
        onClick = { launcher.launch("application/pdf") }, // Cho phép chọn file PDF
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Chọn file PDF")
    }
}