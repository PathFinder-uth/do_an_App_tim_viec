package com.example.pathfinder.ui.screen.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pathfinder.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDarkModeDialog by remember { mutableStateOf(false) }

    if (showDarkModeDialog) {
        DarkModeSelectionDialog(
            currentSelection = uiState.darkModePreference,
            onSelection = { newPreference ->
                viewModel.updateSetting("darkModePreference", newPreference)
                showDarkModeDialog = false
            },
            onDismiss = { showDarkModeDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cài đặt") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF67C2F6))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // --- Nhóm Cài đặt chung ---
            SettingsGroup(title = "Cài đặt chung") {
                // Cài đặt thông báo
                SettingsSwitchItem(
                    title = "Thông báo đẩy",
                    subtitle = "Nhận thông báo về công việc mới và cập nhật từ nhà tuyển dụng.",
                    checked = uiState.pushNotificationsEnabled,
                    onCheckedChange = { viewModel.updateSetting("pushNotificationsEnabled", it) }
                )
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                SettingsSwitchItem(
                    title = "Thông báo qua Email",
                    subtitle = "Nhận bản tin và các thông báo quan trọng qua email.",
                    checked = uiState.emailNotificationsEnabled,
                    onCheckedChange = { viewModel.updateSetting("emailNotificationsEnabled", it) }
                )
                Divider(modifier = Modifier.padding(horizontal = 16.dp))

                // Cài đặt giao diện
                SettingsSelectionItem(
                    title = "Chế độ tối",
                    currentSelection = uiState.darkModePreference.replaceFirstChar { it.uppercase() },
                    onClick = { showDarkModeDialog = true }
                )
            }

            // TODO: Thêm các nhóm cài đặt khác ở đây (Quản lý tài khoản, Về ứng dụng,...)
        }
    }
}

@Composable
private fun DarkModeSelectionDialog(
    currentSelection: String,
    onSelection: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val themeOptions = listOf("light" to "Sáng", "dark" to "Tối", "system" to "Theo hệ thống")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Chọn chế độ tối") },
        text = {
            Column(Modifier.selectableGroup()) {
                themeOptions.forEach { (key, text) ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (key == currentSelection),
                                onClick = { onSelection(key) },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (key == currentSelection),
                            onClick = null // null recommended for accessibility with screenreaders
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}

// Composable phụ để tạo một nhóm cài đặt
@Composable
private fun SettingsGroup(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        content()
    }
}

// Composable phụ cho một mục cài đặt có công tắc (Switch)
@Composable
private fun SettingsSwitchItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 16.sp)
            Text(text = subtitle, fontSize = 14.sp, color = Color.Gray, lineHeight = 20.sp)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

// Composable phụ cho một mục cài đặt có lựa chọn (như dropdown)
@Composable
private fun SettingsSelectionItem(
    title: String,
    currentSelection: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 16.sp, modifier = Modifier.weight(1f))
        Text(text = currentSelection, fontSize = 16.sp, color = Color.Gray)
    }
}
