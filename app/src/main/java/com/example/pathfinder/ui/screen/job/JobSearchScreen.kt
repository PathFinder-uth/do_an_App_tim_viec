package com.example.pathfinder.ui.screen.job

import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pathfinder.viewmodel.JobSearchViewModel
import com.example.pathfinder.data.model.Job
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavHostController
import com.example.pathfinder.navigation.Screen

@Composable
fun JobSearchScreen(viewModel: JobSearchViewModel, navController: NavHostController) {
    // State để lưu trữ từ khóa tìm kiếm
    var query = remember { mutableStateOf("") }

    // Lắng nghe jobResults từ ViewModel
    val jobResults = viewModel.jobResults

    Column(modifier = Modifier.padding(16.dp)) {
        // TextField cho việc nhập từ khóa tìm kiếm
        TextField(
            value = query.value,
            onValueChange = { query.value = it },
            label = { Text("Search for Jobs") },
            modifier = Modifier
                .fillMaxWidth(), // Removed .background(Color.White) here, as containerColor handles it
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color(0xFF67C2F6), // Màu viền khi focus
                unfocusedIndicatorColor = Color.Gray,  // Màu viền khi không focus
                focusedTextColor = Color.Black, // Màu chữ khi focus
                unfocusedTextColor = Color.Black, // Màu chữ khi không focus
                focusedContainerColor = Color.White, // Nền ô nhập màu trắng khi focus
                unfocusedContainerColor = Color.White // Nền ô nhập màu trắng khi không focus
            ),
            singleLine = true,
            keyboardActions = KeyboardActions(
                onDone = {
                    // Khi nhấn Enter sẽ gọi tìm kiếm
                    viewModel.searchJobs(query.value)
                }
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done // Thiết lập phím Enter là Done
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Nút tìm kiếm có nền xanh nhạt, ô nhập màu trắng
        Button(
            onClick = {
                viewModel.searchJobs(query.value)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF67C2F6)) // Nền xanh nhạt
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hiển thị kết quả tìm kiếm từ jobResults
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(jobResults) { job ->
                JobCard(job = job, onClick = {
                    // Xử lý sự kiện khi click vào JobCard (ví dụ: điều hướng sang trang chi tiết công việc)
                    navController.navigate(Screen.JobDetail.withArgs(job.id))
                })
            }
        }
    }
}

@Composable
fun JobItem(job: Job) {
    Column(modifier = Modifier.padding(8.dp)) {
        // Sử dụng kiểu chữ bodyLarge trong Material3
        Text(text = job.title, style = MaterialTheme.typography.bodyLarge)

        // Sử dụng kiểu chữ bodyMedium trong Material3
        Text(text = job.companyName, style = MaterialTheme.typography.bodyMedium)
    }
}
