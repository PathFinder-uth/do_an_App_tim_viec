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
import androidx.navigation.NavController
import com.example.pathfinder.data.local.SessionManager
import com.example.pathfinder.data.repository.AuthRepository
import com.example.pathfinder.navigation.Screen

val jobCategories = listOf(
    "\uD83D\uDCC8 Marketing",
    "Accountant",
    "\uD83D\uDCC8 Auditor",
    "\uD83D\uDD0D Software Engineer",
    "UI/UX Designer",
    "\uD83D\uDCC8 Project Manager",
    "Sales Representative",
    "\uD83D\uDCC8 Financial Manager",
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

    // Load profile on screen enter
    LaunchedEffect(Unit) {
        viewModel.loadUserProfile()
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
                }
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
                            .background(Color.White, shape = MaterialTheme.shapes.medium),
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
                    CustomFlowRow(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        items = jobCategories,
                        itemSpacing = 12.dp,
                        rowSpacing = 12.dp
                    ) { category ->
                        Box(
                            modifier = Modifier
                                .background(Color.White, shape = MaterialTheme.shapes.medium)
                                .clickable { /* TODO: Navigate to filtered jobs */ }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(text = category)
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "C\u00f4ng vi\u1ec7c m\u1edbi nh\u1ea5t :",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(100.dp)
                            .background(Color.White, shape = MaterialTheme.shapes.medium),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "(Danh s\u00e1ch c\u00f4ng vi\u1ec7c s\u1ebd \u0111\u01b0\u1ee3c hi\u1ec3n th\u1ecb \u1edf \u0111\u00e2y)", color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(32.dp))
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