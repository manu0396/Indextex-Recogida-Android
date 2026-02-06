package com.example.feature_settings.ui.screens

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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.components.ZdsSettingsCell
import com.example.feature_settings.model.SettingsEffect
import com.example.feature_settings.model.SettingsUiEvent
import com.example.feature_settings.model.SettingsUiState
import com.example.feature_settings.viewmodel.SettingsViewModel
import com.inditex.dssdkand.accordion.ZDSAccordion
import com.inditex.dssdkand.theme.ZDSTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsRoute(
    onBackClick: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SettingsEffect.NavigateBack -> onBackClick()
            }
        }
    }

    ZDSTheme {
        SettingsScreen(
            state = state,
            onBackClick = { viewModel.onEvent(SettingsUiEvent.OnBackClicked) },
            onEvent = viewModel::onEvent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onBackClick: () -> Unit,
    onEvent: (SettingsUiEvent) -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    var configOpened by remember { mutableStateOf(true) }
    var statusOpened by remember { mutableStateOf(true) }
    val brandNavy = Color(0xFF003366)
    val configuration = LocalConfiguration.current
    val responsiveIconSize = remember(configuration.screenWidthDp) {
        when {
            configuration.screenWidthDp < 360 -> 32.dp
            configuration.screenWidthDp < 600 -> 40.dp
            else -> 56.dp
        }
    }

    Scaffold(
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Configuración",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 22.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = brandNavy
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(top = 16.dp, bottom = 48.dp)
        ) {
            item {
                ZdsSettingsCell(
                    title = "Sincronización",
                    subtitle = state.lastSync ?: "No sincronizado",
                    iconResource = android.R.drawable.stat_notify_sync,
                    iconSize = responsiveIconSize,
                    iconTint = brandNavy,
                    minHeight = 100.dp,
                    onClick = { onEvent(SettingsUiEvent.OnSyncClicked) }
                )
            }

            item {
                ZDSAccordion(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 24.dp),
                    title = "PARÁMETROS DE RUTA",
                    opened = configOpened,
                    onChange = { configOpened = it }
                ) {
                    Column {
                        ZdsSettingsCell(
                            title = "Centro Logístico",
                            subtitle = state.center,
                            iconResource = android.R.drawable.ic_dialog_map,
                            iconSize = responsiveIconSize,
                            iconTint = brandNavy
                        )
                        ZdsSettingsCell(
                            title = "Trayecto Actual",
                            subtitle = state.trayecto,
                            iconResource = android.R.drawable.ic_menu_compass,
                            iconSize = responsiveIconSize,
                            iconTint = brandNavy
                        )
                        ZdsSettingsCell(
                            title = "Modo de Terminal",
                            subtitle = state.mode,
                            iconResource = android.R.drawable.ic_menu_camera,
                            iconSize = responsiveIconSize,
                            iconTint = brandNavy
                        )
                    }
                }
            }

            item {
                ZDSAccordion(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 24.dp),
                    title = "ESTADO DEL DISPOSITIVO",
                    opened = statusOpened,
                    onChange = { statusOpened = it }
                ) {
                    Column {
                        ZdsSettingsCell(
                            title = "Entorno",
                            subtitle = state.environment,
                            iconResource = android.R.drawable.ic_menu_manage,
                            iconSize = responsiveIconSize,
                            iconTint = brandNavy
                        )
                        ZdsSettingsCell(
                            title = "Conexión",
                            subtitle = "Sincronizado / Operativo",
                            iconResource = android.R.drawable.stat_sys_warning,
                            iconSize = responsiveIconSize,
                            iconTint = brandNavy
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(64.dp))
                Text(
                    text = "VERSION: ${state.appVersion}",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Black,
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
