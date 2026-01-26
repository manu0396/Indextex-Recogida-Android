package com.example.recogidas_presentation.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.domain.model.ScanResult
import com.example.recogidas_presentation.R
import com.example.recogidas_presentation.components.ScanResultFeedback
import com.example.recogidas_presentation.ui.screens.states.ScannerStage
import com.example.recogidas_presentation.viewmodel.ScannerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(
    viewModel: ScannerViewModel,
    onBack: (() -> Unit)? = null
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val focusRequester = remember { FocusRequester() }
    val haptic = LocalHapticFeedback.current
    var showManualInput by remember { mutableStateOf(false) }
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasCameraPermission = granted }
    )
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasCameraPermission = ContextCompat.checkSelfPermission(
                    context, Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    LaunchedEffect(state.scanStatus) {
        when (state.scanStatus) {
            is ScanResult.Success -> haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            is ScanResult.Error, is ScanResult.FormatError -> haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            else -> {}
        }
    }
    LaunchedEffect(hasCameraPermission) {
        if (!hasCameraPermission) launcher.launch(Manifest.permission.CAMERA)
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.scanner_title)) },
                navigationIcon = {
                    onBack?.let {
                        IconButton(onClick = it) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .focusRequester(focusRequester)
                .focusable(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (hasCameraPermission && state.isScanning) {
                    CameraPreviewScreen(
                        torchEnabled = state.isTorchOn,
                        isScanPending = state.isScanPending,
                        onQrDetected = viewModel::onCodeScanned,
                        lifecycleOwner = lifecycleOwner,
                        modifier = Modifier.fillMaxSize()
                    )
                } else if (!hasCameraPermission) {
                    Text(
                        text = stringResource(R.string.camera_permission_missing),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                ScanResultFeedback(
                    result = state.scanStatus,
                    modifier = Modifier.align(Alignment.Center).zIndex(10f)
                )
                AnimatedVisibility(
                    visible = state.stage == ScannerStage.DETECTING,
                    modifier = Modifier.align(Alignment.Center).zIndex(1f),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        text = stringResource(R.string.scanner_status_scanning),
                        modifier = Modifier.padding(bottom = 120.dp),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                ScannerOverlay(
                    isTorchOn = state.isTorchOn,
                    isScanning = state.isScanPending,
                    onTorchToggle = viewModel::toggleTorch,
                    onManualEntry = { showManualInput = true },
                    onScanClick = viewModel::onScanTriggerPressed
                )
                if (showManualInput) {
                    ManualEntryDialog(
                        onDismiss = { showManualInput = false },
                        onConfirm = { code ->
                            if (code.isNotBlank()) {
                                viewModel.onManualCodeEntered(code)
                                showManualInput = false
                            }
                        }
                    )
                }
            }
        }
    }
}
@Composable
fun ManualEntryDialog(onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var inputState by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.scanner_manual_entry_hint)) },
        text = {
            OutlinedTextField(
                value = inputState,
                onValueChange = { inputState = it },
                label = { Text(stringResource(R.string.input_label_code)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(inputState) }) {
                Text(stringResource(R.string.scanner_manual_entry_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel))
            }
        }
    )
}

@Composable
fun ScannerOverlay(
    isTorchOn: Boolean,
    isScanning: Boolean,
    onTorchToggle: () -> Unit,
    onManualEntry: () -> Unit,
    onScanClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        FilledTonalIconButton(
            onClick = onTorchToggle,
            modifier = Modifier.align(Alignment.TopEnd).size(64.dp)
        ) {
            Icon(
                imageVector = if (isTorchOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                contentDescription = if (isTorchOn) "Flash On" else "Flash Off"
            )
        }

        Row(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onManualEntry,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.scanner_manual_entry_button))
            }

            LargeScanButton(isScanning = isScanning, onClick = onScanClick)
        }
    }
}

@Composable
fun LargeScanButton(isScanning: Boolean, onClick: () -> Unit) {
    FilledIconButton(
        onClick = onClick,
        modifier = Modifier.size(80.dp),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = if (isScanning) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
        )
    ) {
        Icon(
            imageVector = if (isScanning) Icons.Rounded.Close else Icons.Rounded.QrCodeScanner,
            contentDescription = if (isScanning) "Stop Scan" else "Start Scan",
            modifier = Modifier.size(40.dp)
        )
    }
}
