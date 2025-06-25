package com.example.pathfinder.ui.screen.recruiter

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.pathfinder.navigation.Screen
import com.example.pathfinder.ui.component.rememberUCropLauncher
import com.example.pathfinder.viewmodel.RecruiterInfoViewModel
import androidx.navigation.NavController

@Composable
fun RecruiterInfoScreen(viewModel: RecruiterInfoViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val isLoading = uiState.isSubmitting

    LaunchedEffect(uiState.isSubmitted) {
        if (uiState.isSubmitted) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.ConfirmInfo.route) { inclusive = true }
            }
            viewModel.resetSubmissionState()
        }
    }

    val launchCrop = rememberUCropLauncher(context = context) { croppedUri ->
        viewModel.uploadLogo(croppedUri, context)
    }
    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { launchCrop(it) }
        }
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) pickImageLauncher.launch("image/*")
            else Toast.makeText(context, "Bạn cần cấp quyền để chọn ảnh", Toast.LENGTH_SHORT).show()
        }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF67C2F6))
            .padding(24.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text("Thông tin nhà tuyển dụng", fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                            .clickable {
                                val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                                    Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE
                                permissionLauncher.launch(permission)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        val painter = rememberAsyncImagePainter(model = uiState.logoUrl)
                        Image(
                            painter = painter,
                            contentDescription = null,
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                        )
                    }

                    Button(
                        onClick = {
                            val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                                Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE
                            permissionLauncher.launch(permission)
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text("Chọn ảnh", color = Color.White)
                    }
                }
            }


            item {
            RecruiterTextField(
                "Tên công ty",
                uiState.companyName,
                viewModel::onCompanyNameChange,
                uiState.companyNameError
            )
        }
        item {
            RecruiterTextField(
                "Mô tả công ty",
                uiState.companyDescription,
                viewModel::onDescriptionChange,
                uiState.companyDescriptionError
            )
        }
        item {
            RecruiterTextField(
                "Địa chỉ công ty",
                uiState.companyAddress,
                viewModel::onAddressChange,
                uiState.companyAddressError
            )
        }
        item {
            RecruiterTextField(
                "Số điện thoại liên hệ",
                uiState.phoneNumber,
                viewModel::onPhoneChange,
                uiState.phoneNumberError
            )
        }
        item {
            RecruiterTextField(
                "Email liên hệ",
                uiState.email,
                viewModel::onEmailChange,
                uiState.emailError
            )
        }
        item {
            RecruiterTextField(
                "Website công ty",
                uiState.website,
                viewModel::onWebsiteChange,
                uiState.websiteError
            )
        }

        item {
            DropdownSelector(
                label = "Ngành nghề hoạt động",
                options = listOf("Công nghệ", "Giáo dục", "Tài chính", "Marketing", "Y tế"),
                selected = uiState.industry,
                onSelected = viewModel::onIndustryChange
            )
            uiState.industryError?.let {
                Text(it, color = Color.Red, fontSize = 12.sp)
            }
        }

        item {
            DropdownSelector(
                label = "Quy mô công ty",
                options = listOf(
                    "1-10 người",
                    "11-50 người",
                    "51-200 người",
                    "201-500 người",
                    ">500 người"
                ),
                selected = uiState.companySize,
                onSelected = viewModel::onSizeChange
            )
            uiState.companySizeError?.let {
                Text(it, color = Color.Red, fontSize = 12.sp)
            }
        }

        item {
            Button(
                onClick = viewModel::onSubmit,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                enabled = !isLoading
            ) {
                if (isLoading) CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White
                )
                else Text("Xác nhận", color = Color.White)
            }
        }
    }
}
}

@Composable
fun RecruiterTextField(label: String, value: String, onValueChange: (String) -> Unit, error: String?) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            isError = error != null
        )
        error?.let {
            Text(it, color = Color.Red, fontSize = 12.sp)
        }
    }
}

@Composable
fun DropdownSelector(label: String, options: List<String>, selected: String, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontWeight = FontWeight.Medium)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .background(Color.White, shape = RoundedCornerShape(6.dp))
                .padding(12.dp)
        ) {
            Text(text = selected.ifBlank { "Chọn $label" }, color = if (selected.isBlank()) Color.Gray else Color.Black)
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
