package com.example.pathfinder.ui.screen.home
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.pathfinder.ui.component.DrawerContent
import com.example.pathfinder.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.pathfinder.data.local.SessionManager
import com.example.pathfinder.data.repository.AuthRepository
import com.example.pathfinder.di.AppContainer
import com.example.pathfinder.navigation.Screen
import com.example.pathfinder.ui.component.JobCardSkeleton
import com.example.pathfinder.viewmodel.JobViewModel
import java.text.SimpleDateFormat
import java.util.*
import com.example.pathfinder.data.model.Job
import androidx.compose.ui.draw.blur
val jobCategories = listOf(
    "Marketing",
    "Accountant",
    "Auditor",
    "Software Engineer",
    "UI/UX Designer",
    "Project Manager",
    "Sales Representative",
    "Financial Manager",
    "Consultant"
)

@Composable
fun HomeScreen(viewModel: ProfileViewModel, activity: ComponentActivity, navController: NavController,
               sessionManager: SessionManager,
               authRepository: AuthRepository
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val uiState by viewModel.uiState.collectAsState()
    var showExitDialog by remember { mutableStateOf(false) }
    val jobViewModel: JobViewModel = viewModel(factory = AppContainer.jobViewModelFactory)
    val jobState by jobViewModel.uiState.collectAsState()
    var selectedCategory by remember { mutableStateOf<String?>("Tất cả") }
    val isUserPremium = uiState.isPremium
    // Load profile on screen enter
    LaunchedEffect(Unit) {
        viewModel.loadUserProfile()
    }
    LaunchedEffect(Unit) {
        jobViewModel.fetchAllJobs()
    }
    BackHandler {
        showExitDialog = true
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Xác nhận thoát") },
            text = { Text("Bạn có muốn thoát ứng dụng không?") },
            confirmButton = {
                TextButton(onClick = { activity.finish() }) {
                    Text("Xác nhận")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
    ModalNavigationDrawer(
        drawerContent = {
            DrawerContent(
                fullName = uiState.fullName,
                avatarUrl = uiState.avatarUrl,
                onLogout = {
                    scope.launch {
                        authRepository.logout()
                        sessionManager.clearSession()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0)
                        }
                    }
                },
                navController = navController
            )
        },
        drawerState = drawerState
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF67C2F6)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    scope.launch { drawerState.open() }
                                }
                        )
                    }

                    if (isUserPremium) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "⭐ Tài khoản Premium",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD4AF37)
                        )
                    }
                    Text(
                        text = "PathFinder",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "\"Connecting talent with opportunity.\"",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(48.dp)
                            .background(Color.White, shape = MaterialTheme.shapes.medium)
                            .clickable {
                                navController.navigate(Screen.JobSearch.route)
                            },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "Search for companies / jobs",
                            modifier = Modifier.padding(start = 16.dp),
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Option:",
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    val allCategoriesWithAllOption = listOf("Tất cả") + jobCategories

                    CustomFlowRow(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        items = allCategoriesWithAllOption,
                        itemSpacing = 12.dp,
                        rowSpacing = 12.dp
                    ) { category ->
                        val isSelected = category == selectedCategory

                        Button(
                            onClick = {
                                selectedCategory = category
                                if (category == "Tất cả") {
                                    // Nếu chọn "Tất cả", tải lại toàn bộ công việc
                                    jobViewModel.fetchAllJobs()
                                } else {
                                    // Nếu chọn ngành khác, gọi hàm lọc
                                    jobViewModel.filterJobsByCategory(category)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.White,
                                contentColor = if (isSelected) Color.White else Color.Black
                            )
                        ) {
                            Text(text = category)
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "Công việc mới nhất:",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // --- ĐÃ SỬA LẠI CẤU TRÚC KHỐI NÀY ---
                if (jobState.isLoading) {
                    // Hiển thị 5 skeleton card khi đang tải
                    items(5) {
                        JobCardSkeleton()
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                } else if (jobState.jobs.isEmpty()) {
                    item {
                        Text(
                            text = "Hiện chưa có công việc nào.",
                            color = Color.Gray,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                } else {
                    // Sử dụng items để hiển thị danh sách công việc
                    items(jobState.jobs.take(5)) { job ->
                        JobCardForCandidate(
                            job = job,
                            isPremiumUser = isUserPremium,
                            onUpgradeClick = { navController.navigate(Screen.Premium.route) },
                            onJobClick = {
                                // Logic: Chỉ cho phép xem chi tiết nếu là job free hoặc user là premium
                                if (job.jobType == "free" || isUserPremium) {
                                    navController.navigate(Screen.JobDetail.withArgs(job.id))
                                } else {
                                    // Nếu không, điều hướng đến màn hình premium
                                    navController.navigate(Screen.Premium.route)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
private fun JobCardForCandidate(
    job: Job,
    isPremiumUser: Boolean,
    onJobClick: () -> Unit,
    onUpgradeClick: () -> Unit
) {
    // Logic để xác định công việc có bị khóa hay không
    val isLocked = job.jobType == "premium" && !isPremiumUser
    // Áp dụng hiệu ứng làm mờ nếu bị khóa
    val cardModifier = if (isLocked) Modifier.blur(4.dp) else Modifier
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    Box(contentAlignment = Alignment.Center) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                // Chỉ cho phép nhấn vào card nếu nó không bị khóa
                .clickable(enabled = !isLocked, onClick = onJobClick),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = cardModifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (job.logoUrl.isNotBlank()) {
                        Image(
                            painter = rememberAsyncImagePainter(job.logoUrl),
                            contentDescription = "Company Logo",
                            modifier = Modifier.size(40.dp).clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = job.companyName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = job.title, fontWeight = FontWeight.Bold)
                Text(text = job.location, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Ngày đăng: ${dateFormat.format(Date(job.createdAt))}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Hạn cuối: ${dateFormat.format(Date(job.deadline))}",
                        fontSize = 12.sp,
                        color = Color.Red
                    )
                }
            }
        }

        // Nếu công việc bị khóa, hiển thị lớp phủ mờ và nút nâng cấp
        if (isLocked) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("⭐ Công việc Premium", fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onUpgradeClick) {
                    Text("Nâng cấp để xem")
                }
            }
        }
    }
}
    @Composable
    fun CustomFlowRow(
        modifier: Modifier = Modifier,
        items: List<String>,
        itemSpacing: Dp = 12.dp,
        rowSpacing: Dp = 12.dp,
        itemContent: @Composable (String) -> Unit
    ) {
        val screenWidth = LocalConfiguration.current.screenWidthDp.dp
        var currentRowWidth = 0.dp
        var currentRow = mutableListOf<String>()
        val rows = mutableListOf<List<String>>()

        items.forEach { item ->
            val itemWidth = (item.length * 8).dp + 32.dp
            if (currentRowWidth + itemWidth + itemSpacing > screenWidth - 32.dp) {
                rows.add(currentRow)
                currentRow = mutableListOf(item)
                currentRowWidth = itemWidth
            } else {
                currentRow.add(item)
                currentRowWidth += itemWidth + itemSpacing
            }
        }
        if (currentRow.isNotEmpty()) rows.add(currentRow)

        Column(modifier = modifier) {
            rows.forEach { row ->
                Row(modifier = Modifier.padding(bottom = rowSpacing)) {
                    row.forEachIndexed { index, item ->
                        Box(modifier = Modifier.padding(end = if (index != row.lastIndex) itemSpacing else 0.dp)) {
                            itemContent(item)
                        }
                    }
                }
            }
        }
    }

