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
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.pathfinder.ui.component.rememberUCropLauncher
import com.example.pathfinder.viewmodel.RecruiterInfoViewModel
import com.example.pathfinder.navigation.Screen

@Composable
fun RecruiterUpdateInfoScreen(viewModel: RecruiterInfoViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val isLoading = uiState.isSubmitting

    val launchCrop = rememberUCropLauncher(context = context) { croppedUri ->
        viewModel.uploadLogo(croppedUri, context)
    }
    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { launchCrop(it) }
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) pickImageLauncher.launch("image/*")
        else Toast.makeText(context, "Bạn cần cấp quyền để chọn ảnh", Toast.LENGTH_SHORT).show()
    }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF67C2F6))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Cập nhật thông tin nhà tuyển dụng", fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .clickable {
                    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE
                    permissionLauncher.launch(permission)
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(uiState.logoUrl),
                contentDescription = "Logo",
                modifier = Modifier.size(100.dp).clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        RecruiterTextField("Tên công ty", uiState.companyName, viewModel::onCompanyNameChange, uiState.companyNameError)
        RecruiterTextField("Mô tả công ty", uiState.companyDescription, viewModel::onDescriptionChange, uiState.companyDescriptionError)
        RecruiterTextField("Địa chỉ", uiState.companyAddress, viewModel::onAddressChange, uiState.companyAddressError)
        RecruiterTextField("Số điện thoại", uiState.phoneNumber, viewModel::onPhoneChange, uiState.phoneNumberError)
        RecruiterTextField("Email", uiState.email, viewModel::onEmailChange, uiState.emailError)
        RecruiterTextField("Website", uiState.website, viewModel::onWebsiteChange, uiState.websiteError)

        DropdownSelector("Ngành nghề", listOf("Công nghệ", "Giáo dục", "Tài chính", "Marketing", "Y tế"), uiState.industry, viewModel::onIndustryChange)
        uiState.industryError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        DropdownSelector("Quy mô công ty", listOf("1-10 người", "11-50 người", "51-200 người", "201-500 người", ">500 người"), uiState.companySize, viewModel::onSizeChange)
        uiState.companySizeError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Quay lại")
            }

            Button(
                onClick = viewModel::onSubmit,
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(8.dp)
            ) {
                if (isLoading) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                else Text("Lưu", color = Color.White)
            }
        }
    }
}
