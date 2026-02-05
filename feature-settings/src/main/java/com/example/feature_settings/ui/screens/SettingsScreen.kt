package com.example.feature_settings.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.components.SettingsCard
import com.example.components.SettingsRow
import com.example.feature_settings.model.SettingsUiEvent
import com.example.feature_settings.model.SettingsUiState
import com.example.feature_settings.model.SyncStatus
import com.example.feature_settings.viewmodel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsRoute(
    onBackClick: () -> Unit,
    onLogoutSuccess: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.userEmail) {
        if (state.userEmail == null && !state.isLoading) {
            onLogoutSuccess()
        }
    }
    SettingsScreen(
        state = state,
        onBackClick = onBackClick,
        onEvent = { event ->
            if (event is SettingsUiEvent.OnLogoutClicked) {
                onLogoutSuccess()
            }
            viewModel.onEvent(event)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onBackClick: () -> Unit,
    onEvent: (SettingsUiEvent) -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.syncStatus) {
        when (state.syncStatus) {
            is SyncStatus.Success -> snackBarHostState.showSnackbar(state.syncStatus.message)
            is SyncStatus.Error -> snackBarHostState.showSnackbar(
                message = state.syncStatus.message,
                duration = SnackbarDuration.Long
            )
            else -> {}
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Configuración", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SettingsCard {
                    SettingsRow(
                        icon = Icons.Default.Sync,
                        title = "Sincronización",
                        subtitle = state.lastSync,
                        onClick = {
                            onEvent(SettingsUiEvent.OnSyncClicked)
                        }
                    )
                }
            }

            item {
                SettingsCard {
                    Column {
                        SettingsRow(Icons.Default.Business, "Centro", state.center)
                        HorizontalDivider(modifier = Modifier.padding(start = 56.dp), thickness = 0.5.dp)
                        SettingsRow(Icons.Default.Route, "Trayecto", state.trayecto)
                        HorizontalDivider(modifier = Modifier.padding(start = 56.dp), thickness = 0.5.dp)
                        SettingsRow(Icons.Default.QrCodeScanner, "Modo", state.mode)
                    }
                }
            }

            item {
                SettingsCard {
                    Column {
                        SettingsRow(Icons.Default.Layers, "Entorno", state.environment)
                        HorizontalDivider(modifier = Modifier.padding(start = 56.dp), thickness = 0.5.dp)
                        SettingsRow(
                            icon = Icons.Default.WarningAmber,
                            title = "Errores en las autorizaciones",
                            subtitle = "Sin conexión",
                            iconColor = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            item {
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { onEvent(SettingsUiEvent.OnLogoutClicked) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Cerrar Sesión")
                }
            }

            item {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Versión ${state.appVersion}",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
