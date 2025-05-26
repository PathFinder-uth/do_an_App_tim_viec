package com.example.pathfinder.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.* // Sử dụng Material Design 3
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pathfinder.R
import com.example.pathfinder.ui.theme.ApptimViecTheme// Đảm bảo R được import đúng, nếu bạn có resource icons
// Giả sử bạn có file theme của riêng bạn

@OptIn(ExperimentalMaterial3Api::class) // Đánh dấu nếu bạn dùng các API thử nghiệm của Material3
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit, // Hàm callback khi đăng nhập thành công
    onForgotPasswordClick: () -> Unit,
    onCreateAccountClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Màu nền giống trong ảnh
    val backgroundColor = Color(0xFF67C2F6) // Một màu xanh dương nhạt tương tự

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Tiêu đề "Đăng nhập"
            Text(
                text = "Đăng nhập",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black, // Hoặc màu phù hợp với theme của bạn
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Mô tả
            Text(
                text = "Nhập Email để đăng nhập tài khoản người dùng",
                fontSize = 16.sp,
                color = Color.DarkGray, // Hoặc màu phù hợp
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Email TextField
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("email@domain.com") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    // Các tham số khác của border/label/text color nếu muốn tùy chỉnh
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray
                    // Đảm bảo thêm các tham số cần thiết khác nếu bạn muốn tùy chỉnh sâu hơn.
                    // Ví dụ:
                    // focusedLabelColor = Color.DarkGray,
                    // unfocusedLabelColor = Color.Gray,
                    // textColor = Color.Black
                )
            )

            // Password TextField
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    // Các tham số khác của border/label/text color nếu muốn tùy chỉnh
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray
                    // Đảm bảo thêm các tham số cần thiết khác nếu bạn muốn tùy chỉnh sâu hơn.
                    // Ví dụ:
                    // focusedLabelColor = Color.DarkGray,
                    // unfocusedLabelColor = Color.Gray,
                    // textColor = Color.Black
                )
            )

            // Continue Button
            Button(
                onClick = { /* TODO: Xử lý logic đăng nhập */ onLoginSuccess() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black), // Hoặc màu đen như trong ảnh
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Continue", color = Color.White, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quên mật khẩu?
            TextButton(onClick = onForgotPasswordClick) {
                Text(
                    text = "Quên mật khẩu?",
                    color = MaterialTheme.colorScheme.primary, // Hoặc một màu xanh đậm
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Divider "hoặc"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
                Text(
                    text = " hoặc ",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Google Login Button
            Button(
                onClick = { /* TODO: Xử lý đăng nhập Google */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                // Giả sử bạn có icon Google trong drawable
                // Nếu chưa, bạn cần thêm nó vào res/drawable
                // Ví dụ: painterResource(id = R.drawable.ic_google)
                // Hoặc dùng Icon từ thư viện compose-icons-extended nếu có
                // Icon(Icons.Filled.AccountCircle, contentDescription = "Google Icon") // Thay thế bằng icon Google thực tế
                Image(
                    painter = painterResource(id = R.drawable.ic_google_logo), // Thay thế bằng icon Google của bạn
                    contentDescription = "Google Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Đăng nhập với Google", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Facebook Login Button
            Button(
                onClick = { /* TODO: Xử lý đăng nhập Facebook */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                // Giả sử bạn có icon Facebook trong drawable
                Image(
                    painter = painterResource(id = R.drawable.ic_facebook_logo), // Thay thế bằng icon Facebook của bạn
                    contentDescription = "Facebook Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Đăng nhập với Facebook", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Agreement Text
            Text(
                text = "By clicking continue, you agree to our ",
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = { /* TODO: Mở Terms of Service */ }) {
                    Text(
                        text = "Terms of Service",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    text = " and ",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                TextButton(onClick = { /* TODO: Mở Privacy Policy */ }) {
                    Text(
                        text = "Privacy Policy",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Tạo tài khoản
            TextButton(onClick = onCreateAccountClick) {
                Text(
                    text = "Tạo tài khoản",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// Preview để xem trước giao diện trong Android Studio
@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    ApptimViecTheme { // Sử dụng theme của ứng dụng bạn
        LoginScreen(
            onLoginSuccess = {},
            onForgotPasswordClick = {},
            onCreateAccountClick = {}
        )
    }
}