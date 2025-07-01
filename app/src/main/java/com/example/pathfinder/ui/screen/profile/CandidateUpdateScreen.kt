package com.example.pathfinder.ui.screen.profile

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.pathfinder.navigation.Screen
import com.example.pathfinder.ui.component.rememberUCropLauncher
import com.example.pathfinder.viewmodel.ProfileViewModel

@Composable
fun CandidateUpdateScreen(viewModel: ProfileViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val isSubmitted by viewModel.isSubmitted.collectAsState()

    // Xử lý ảnh đại diện
    val launchCrop = rememberUCropLauncher(context = context) { uri ->
        viewModel.uploadAvatar(uri, context)
    }
    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { launchCrop(it) }
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) pickImageLauncher.launch("image/*")
        else Toast.makeText(context, "Bạn cần cấp quyền để chọn ảnh", Toast.LENGTH_SHORT).show()
    }

    // Điều hướng quay lại khi submit thành công
    LaunchedEffect(isSubmitted) {
        if (isSubmitted) {
            navController.popBackStack() // Quay về CandidateProfileDetailScreen
            viewModel.resetSubmissionState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF67C2F6))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Cập nhật thông tin cá nhân", fontSize = 22.sp, modifier = Modifier.padding(bottom = 16.dp))

        Image(
            painter = rememberAsyncImagePainter(uiState.avatarUrl),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .clickable {
                    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE
                    permissionLauncher.launch(permission)
                }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = uiState.fullName,
            onValueChange = viewModel::onFullNameChange,
            label = { Text("Họ và tên") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.gender,
            onValueChange = viewModel::onGenderChange,
            label = { Text("Giới tính") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.address,
            onValueChange = viewModel::onAddressChange,
            label = { Text("Địa chỉ") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.phone,
            onValueChange = viewModel::onPhoneChange,
            label = { Text("Số điện thoại") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.birthday,
            onValueChange = viewModel::onBirthdayChange,
            label = { Text("Ngày sinh") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.contact,
            onValueChange = viewModel::onContactChange,
            label = { Text("Thông tin liên lạc") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = viewModel::onSubmit,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("Xác nhận", color = Color.White)
        }
    }
}