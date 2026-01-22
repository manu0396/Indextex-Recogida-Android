package com.example.recogidas_presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.recogidas_presentation.components.CameraPreview
import com.example.recogidas_presentation.components.ErrorView
import com.example.recogidas_presentation.components.ScanOverlay
import com.example.recogidas_presentation.viewmodel.RecogidasViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecogidaScannerScreen(
    modifier: Modifier,
    viewModel: RecogidasViewModel = koinViewModel(),
    onBack: (() -> Unit)? = null
) {
    val state by viewModel.uiState.collectAsState()
    val cameraSelector = viewModel.getCameraSelector()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Recogidas") },
                navigationIcon = {
                    if(onBack != null){
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            else if (state.error != null && state.name.isEmpty()) {
                ErrorView(
                    message = state.error ?: "Unknown error",
                    onRetry = { viewModel.onIntent(RecogidasIntent.SyncData) }
                )
            }
            else {
                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    cameraSelector = cameraSelector,
                    isLaserActive = state.isLaserActive
                )
                ScanOverlay(
                    state = state,
                    onRefresh = { viewModel.onIntent(RecogidasIntent.SyncData) },
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}


