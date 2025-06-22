package com.example.pathfinder.ui.screen.profile

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.pathfinder.viewmodel.ProfileViewModel
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.navigation.NavController
import com.example.pathfinder.navigation.Screen
import com.example.pathfinder.ui.component.rememberUCropLauncher
@Composable
fun ProfileFormScreen(viewModel: ProfileViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val launchCrop = rememberUCropLauncher(context = context) { croppedUri ->
        viewModel.uploadAvatar(croppedUri, context)
    }
    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            launchCrop(it)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            pickImageLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Bạn cần cấp quyền để chọn ảnh", Toast.LENGTH_SHORT).show()
        }
    }
    val isSubmitted by viewModel.isSubmitted.collectAsState()
    LaunchedEffect(isSubmitted) {
        if (isSubmitted) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.ConfirmInfo.route) { inclusive = true }
            }
            viewModel.resetSubmissionState()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF67C2F6))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Thông tin cá nhân",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                                    Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE
                                permissionLauncher.launch(permission)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        val painter = rememberAsyncImagePainter(model = uiState.avatarUrl)
                        Image(
                            painter = painter,
                            contentDescription = "Avatar",
                            modifier = Modifier.size(90.dp).clip(CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                                Manifest.permission.READ_MEDIA_IMAGES
                            else Manifest.permission.READ_EXTERNAL_STORAGE
                            permissionLauncher.launch(permission)
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Cập nhật ảnh", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

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
        }
    }


}
