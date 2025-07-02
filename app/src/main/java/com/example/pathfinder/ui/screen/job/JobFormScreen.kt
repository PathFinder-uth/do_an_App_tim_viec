package com.example.pathfinder.ui.screen.job

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
    recruiterId: String?,
    companyName: String?,
    logoUrl: String?,
    jobIdToEdit: String?
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
    var isTypeDropdownExpanded by remember { mutableStateOf(false) }

    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val isEditMode = jobIdToEdit != null
    val jobToEdit by viewModel.uiState.collectAsState()
    val jobCategories = listOf(
        "Marketing", "Accountant", "Auditor", "Software Engineer",
        "UI/UX Designer", "Project Manager", "Sales Representative",
        "Financial Manager", "Consultant", "Ngành khác" // Thêm lựa chọn "Ngành khác"
    )
    var selectedCategory by remember { mutableStateOf(jobCategories[0]) }
    var isCategoryDropdownExpanded by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(jobIdToEdit) {
        if (isEditMode) {
            viewModel.getJobDetail(jobIdToEdit!!)
        }
    }

    // Điền dữ liệu cũ vào form khi đã tải xong
    LaunchedEffect(uiState.selectedJob) {
        if (isEditMode) {
            uiState.selectedJob?.let { job ->
                title = job.title
                description = job.description
                requirements = job.requirements
                salary = job.salary
                location = job.location
                type = job.type
                deadlineMillis = job.deadline
                selectedJobType = job.jobType
                selectedCategory = job.category
            }
        }
    }
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            delay(1000)
            navController.popBackStack()
            viewModel.clearSuccessFlag() // Reset cờ để không bị lặp lại
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Chỉnh sửa công việc" else "Tạo công việc mới") }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp) // Khoảng cách giữa các item
        ) {
            // Mỗi thành phần giao diện giờ được đặt trong một item()
            item {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Tiêu đề công việc") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Mô tả công việc") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
            item {
                OutlinedTextField(
                    value = requirements,
                    onValueChange = { requirements = it },
                    label = { Text("Yêu cầu ứng viên") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
            item {
                OutlinedTextField(value = salary, onValueChange = { salary = it }, label = { Text("Mức lương") }, modifier = Modifier.fillMaxWidth())
            }
            item {
                OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Địa điểm làm việc") }, modifier = Modifier.fillMaxWidth())
            }
            item {
                OutlinedTextField(value = type, onValueChange = { type = it }, label = { Text("Hình thức làm việc (Full-time, Part-time,... )") }, modifier = Modifier.fillMaxWidth())
            }
            item {
                ExposedDropdownMenuBox(
                    expanded = isCategoryDropdownExpanded,
                    onExpandedChange = { isCategoryDropdownExpanded = !it }
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Ngành nghề") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
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
            }
            item {
                ExposedDropdownMenuBox(
                    expanded = isTypeDropdownExpanded,
                    onExpandedChange = { isTypeDropdownExpanded = !it }
                ) {
                    OutlinedTextField(
                        value = selectedJobType.replaceFirstChar { it.uppercase() },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Loại công việc") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTypeDropdownExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = isTypeDropdownExpanded,
                        onDismissRequest = { isTypeDropdownExpanded = false }
                    ) {
                        jobTypes.forEach { typeValue ->
                            DropdownMenuItem(
                                text = { Text(typeValue.replaceFirstChar { it.uppercase() }) },
                                onClick = {
                                    selectedJobType = typeValue
                                    isTypeDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            item {
                OutlinedButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Chọn hạn nộp: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(deadlineMillis))}")
                }
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        val jobData = Job(
                            id = jobIdToEdit ?: "",
                            recruiterId = if (isEditMode) uiState.selectedJob?.recruiterId ?: "" else recruiterId ?: "",
                            title = title,
                            description = description,
                            requirements = requirements,
                            salary = salary,
                            location = location,
                            type = type,
                            companyName = if (isEditMode) uiState.selectedJob?.companyName ?: "" else companyName ?: "",
                            logoUrl = if (isEditMode) uiState.selectedJob?.logoUrl ?: "" else logoUrl ?: "",
                            createdAt = if (isEditMode) uiState.selectedJob?.createdAt ?: System.currentTimeMillis() else System.currentTimeMillis(),
                            deadline = deadlineMillis,
                            jobType = selectedJobType,
                            category = selectedCategory
                        )
                        if (isEditMode) {
                            viewModel.updateJob(jobData)
                        } else {
                            viewModel.createJob(jobData)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text(if (isEditMode) "Cập nhật công việc" else "Tạo công việc")
                }
            }
            item {
                if (uiState.isLoading) {
                    CircularProgressIndicator()
                }
                if (uiState.isSuccess) {
                    Text("Thao tác thành công!", color = MaterialTheme.colorScheme.primary)
                }
                uiState.error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
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
