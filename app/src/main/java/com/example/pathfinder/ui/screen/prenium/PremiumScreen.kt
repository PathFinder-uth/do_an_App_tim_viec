package com.example.pathfinder.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pathfinder.viewmodel.PremiumViewModel
import com.example.pathfinder.viewmodel.PurchaseState

@Composable
fun PremiumScreen(
    navController: NavController,
    viewModel: PremiumViewModel = viewModel()
) {
    val purchaseState by viewModel.purchaseState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(purchaseState) {
        when (purchaseState) {
            is PurchaseState.Success -> {
                Toast.makeText(context, "Nâng cấp thành công!", Toast.LENGTH_LONG).show()
                // Quay lại màn hình chính sau khi thành công
                navController.popBackStack()
                viewModel.resetState()
            }
            is PurchaseState.Error -> {
                Toast.makeText(context, (purchaseState as PurchaseState.Error).message, Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Nâng cấp Premium",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Mở khóa toàn bộ công việc và nhận được các quyền lợi độc quyền từ nhà tuyển dụng hàng đầu.",
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        if (purchaseState is PurchaseState.Loading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { viewModel.purchasePremium() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Mua ngay (Mô phỏng)")
            }
        }
    }
}
