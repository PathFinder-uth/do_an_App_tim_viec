package com.example.pathfinder.ui.screen.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PolicyScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chính sách & Điều khoản") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Chính sách bảo mật
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Chính sách bảo mật",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Chúng tôi cam kết bảo vệ thông tin cá nhân của bạn. Mọi thông tin được thu thập chỉ nhằm mục đích cải thiện trải nghiệm người dùng, hỗ trợ tìm kiếm việc làm và kết nối với nhà tuyển dụng. Các dữ liệu như email, tên, số điện thoại sẽ không được chia sẻ cho bên thứ ba khi chưa có sự đồng ý của bạn.",
                    fontSize = 14.sp
                )
                Text(
                    text = "Người dùng có quyền truy cập, chỉnh sửa hoặc yêu cầu xóa thông tin cá nhân của mình bất kỳ lúc nào thông qua phần 'Thông tin tài khoản'.",
                    fontSize = 14.sp
                )
                Text(
                    text = "Chúng tôi sử dụng các biện pháp bảo mật kỹ thuật để bảo vệ dữ liệu khỏi truy cập trái phép hoặc sử dụng sai mục đích.",
                    fontSize = 14.sp
                )
            }

            // Điều khoản sử dụng
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Điều khoản sử dụng",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Khi sử dụng ứng dụng này, bạn đồng ý không đăng tải thông tin sai lệch, gây hiểu nhầm hoặc nội dung phản cảm, không phù hợp.",
                    fontSize = 14.sp
                )
                Text(
                    text = "Người dùng chịu trách nhiệm về nội dung trong hồ sơ cá nhân, thông tin CV và các hoạt động ứng tuyển. Mọi hành vi lừa đảo hoặc gian lận sẽ bị xử lý theo pháp luật.",
                    fontSize = 14.sp
                )
                Text(
                    text = "Chúng tôi có quyền tạm khóa hoặc xóa tài khoản nếu phát hiện hành vi vi phạm điều khoản mà không cần báo trước.",
                    fontSize = 14.sp
                )
                Text(
                    text = "Ứng dụng có thể cập nhật điều khoản sử dụng theo thời gian. Người dùng nên kiểm tra định kỳ để nắm rõ các thay đổi.",
                    fontSize = 14.sp
                )
            }
        }
    }
}
