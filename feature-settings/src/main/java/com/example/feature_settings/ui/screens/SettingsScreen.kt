package com.example.feature_settings.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.feature_settings.model.SettingsUiEvent
import com.example.feature_settings.model.SettingsUiState
import com.example.feature_settings.viewmodel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsRoute(
    onBackClick: () -> Unit,
    onLogoutSuccess: () -> Unit, // Callback for navigation
    viewModel: SettingsViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsScreen(
        state = state,
        onBackClick = onBackClick,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    state: SettingsUiState,
    onBackClick: () -> Unit,
    onEvent: (SettingsUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SettingsItem("Version", state.appVersion)
            SettingsItem("Environment", state.environment)
            state.userEmail?.let { SettingsItem("User", it) }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = { onEvent(SettingsUiEvent.OnLogoutClicked) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onError
                    )
                } else {
                    Text("Log Out")
                }
            }
        }
    }
}

@Composable
private fun SettingsItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}
