package com.example.pathfinder.data.ui.screen.register

import android.text.style.BackgroundColorSpan
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.* // Sử dụng Material Design 3
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pathfinder.ui.theme.ApptimViewTheme  // Chắc chắn import đúng theme của bạn
import androidx.compose.runtime.*
import androidx.compose.material.*
import androidx.navigation.NavController
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.material.icons.filled.CheckBox // Corrected import
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.text.input.KeyboardType.Companion
import androidx.compose.ui.Alignment.Horizontal
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.text.input.VisualTransformation
import com.google.firebase.annotations.concurrent.Background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController
) {
    var email by remember{ mutableStateOf("")}
    var password by remember{ mutableStateOf("")}
    var confirmPassword by remember{ mutableStateOf("")}
    var agreeToTerms by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF7DE4F5)).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top)
    {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text="Đăng ký tài khoản",
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(130.dp))

        Text(
            text="Nhập Email",
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = {Text("email@domain.com")},
            placeholder = {Text("email@domain.com")},
            modifier = Modifier.fillMaxWidth().background(Color.White).height(50.dp).padding(bottom = 5.dp).align(Alignment.CenterHorizontally),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier =Modifier.height(12.dp))

        Text(
            text="Nhập Password",
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(5.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            label = { Text("password")},
            placeholder = { Text("password")},
            modifier = Modifier.fillMaxWidth().background(Color.White).height(50.dp).padding(bottom = 5.dp).align(Alignment.CenterHorizontally),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Text(
            text = "Độ dài mật khẩu có ít nhất 8 kí tự, phải bao gồm chữ cái, chữ hoa, số và ký tự đặc biệt (1,@,#,$,%)" ,
            fontSize = 12.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(top = 4.dp), // Corrected 'modifer' to 'modifier'
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text="Xác nhận lại Password",
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {confirmPassword = it},
            label = { Text("password")},
            placeholder = {Text("password")},
            modifier = Modifier.fillMaxWidth().background(Color.White).height(50.dp).padding(bottom = 5.dp).align(Alignment.CenterHorizontally),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth().background(Color.White).clickable { agreeToTerms = !agreeToTerms }
        ) {
            Icon(
                imageVector = if (agreeToTerms)  Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                contentDescription = "Checkbox",
                modifier = Modifier.padding(top = 0.dp),
            )

            Spacer(modifier = Modifier.width(8.dp))
            Text("Đồng ý với các điều khoản và chính sách của PathFinder",
                modifier = Modifier.fillMaxWidth().height(120.dp),
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

        Button(
            onClick = {
                // TODO: Validate và đăng ký tài khoản
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp).padding(bottom =10.dp).background(Color.Black),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            enabled = agreeToTerms,
        ) {
            Text(text = "Continue", color = Color.White)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    // Ensure ApptimViewTheme is correctly defined and applies its content
    // This is where your theme wraps the content it provides styles for.
    ApptimViewTheme {
        // You need to pass a NavController to the RegisterScreen.
        // For preview, you can use rememberNavController()
        RegisterScreen(navController = rememberNavController())
    }
}


