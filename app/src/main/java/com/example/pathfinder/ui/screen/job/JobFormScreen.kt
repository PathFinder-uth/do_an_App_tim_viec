package com.example.pathfinder.ui.screen.job

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pathfinder.data.model.Job
import com.example.pathfinder.viewmodel.JobViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobFormScreen(
    viewModel: JobViewModel,
    navController: NavController,
    recruiterId: String,
    companyName: String,
    logoUrl: String
) {
    // State cho các trường nhập liệu
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var requirements by remember { mutableStateOf("") }
    var salary by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }

    // State cho Date Picker
    var deadlineMillis by remember { mutableStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }

    // --- STATE CHO DROPDOWN PREMIUM ---
    val jobTypes = listOf("free", "premium")
    var selectedJobType by remember { mutableStateOf(jobTypes[0]) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val jobCategories = listOf(
        "Marketing", "Accountant", "Auditor", "Software Engineer",
        "UI/UX Designer", "Project Manager", "Sales Representative",
        "Financial Manager", "Consultant", "Ngành khác" // Thêm lựa chọn "Ngành khác"
    )
    var selectedCategory by remember { mutableStateOf(jobCategories[0]) }
    var isCategoryDropdownExpanded by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Thêm thanh cuộn
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Tiêu đề công việc") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Mô tả công việc") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )
        OutlinedTextField(
            value = requirements,
            onValueChange = { requirements = it },
            label = { Text("Yêu cầu ứng viên") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )
        OutlinedTextField(
            value = salary,
            onValueChange = { salary = it },
            label = { Text("Mức lương") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Địa điểm làm việc") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = type,
            onValueChange = { type = it },
            label = { Text("Hình thức làm việc (Full-time, Part-time,... )") },
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(
            expanded = isCategoryDropdownExpanded,
            // SỬA LỖI: Thay đổi logic để bật/tắt dropdown một cách chính xác
            onExpandedChange = { isCategoryDropdownExpanded = !isCategoryDropdownExpanded },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            OutlinedTextField(
                value = selectedCategory,
                onValueChange = {},
                readOnly = true,
                label = { Text("Ngành nghề") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryDropdownExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor() // Quan trọng: để liên kết TextField với Menu
            )
            ExposedDropdownMenu(
                expanded = isCategoryDropdownExpanded,
                onDismissRequest = { isCategoryDropdownExpanded = false }
            ) {
                jobCategories.forEach { categoryValue ->
                    DropdownMenuItem(
                        text = { Text(categoryValue) },
                        onClick = {
                            selectedCategory = categoryValue
                            isCategoryDropdownExpanded = false
                        }
                    )
                }
            }
        }
        // --- DROPDOWN CHỌN LOẠI CÔNG VIỆC ---
        ExposedDropdownMenuBox(
            expanded = isDropdownExpanded,
            onExpandedChange = { isDropdownExpanded = !isDropdownExpanded },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            OutlinedTextField(
                value = selectedJobType.replaceFirstChar { it.uppercase() },
                onValueChange = {},
                readOnly = true,
                label = { Text("Loại công việc") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false }
            ) {
                jobTypes.forEach { typeValue ->
                    DropdownMenuItem(
                        text = { Text(typeValue.replaceFirstChar { it.uppercase() }) },
                        onClick = {
                            selectedJobType = typeValue
                            isDropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Chọn hạn nộp: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(deadlineMillis))}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val job = Job(
                    recruiterId = recruiterId,
                    title = title,
                    description = description,
                    requirements = requirements,
                    salary = salary,
                    location = location,
                    type = type,
                    companyName = companyName,
                    logoUrl = logoUrl,
                    createdAt = System.currentTimeMillis(),
                    deadline = deadlineMillis,
                    jobType = selectedJobType,
                    category = selectedCategory
                )
                viewModel.createJob(job)
            },
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("Tạo công việc")
        }

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 8.dp))
        }

        if (state.isSuccess) {
            Text("Tạo công việc thành công!", color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 8.dp))
            LaunchedEffect(true) {
                delay(1500)
                navController.popBackStack()
            }
        }

        state.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }
    }

    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = deadlineMillis

        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth, 23, 59, 59)
                deadlineMillis = calendar.timeInMillis
                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}
