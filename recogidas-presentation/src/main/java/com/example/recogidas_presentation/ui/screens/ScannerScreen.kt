package com.example.recogidas_presentation.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.recogidas_presentation.R
import com.example.recogidas_presentation.components.ScanResultFeedback
import com.example.recogidas_presentation.viewmodel.ScannerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(
    viewModel: ScannerViewModel,
    onBack: (() -> Unit)? = null
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    var showManualInput by remember { mutableStateOf(false) }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        }
    )

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.scanner_title)) },
                navigationIcon = {
                    onBack?.let {
                        IconButton(onClick = it) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .focusRequester(focusRequester)
                .focusable()
        ) {
            if (hasCameraPermission) {
                CameraPreviewScreen(
                    torchEnabled = state.isTorchOn,
                    onQrDetected = { code -> viewModel.onCodeScanned(code) }
                )
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.camera_permission_missing))
                }
            }

            if (state.isScanPending) {
                Text(
                    text = stringResource(R.string.scanner_status_scanning),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 120.dp),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            ScanResultFeedback(
                result = state.scanStatus,
                modifier = Modifier.align(Alignment.Center)
            )

            ScannerOverlay(
                isTorchOn = state.isTorchOn,
                isScanning = state.isScanPending,
                onTorchToggle = viewModel::toggleTorch,
                onManualEntry = { showManualInput = true },
                onScanClick = {
                    viewModel.onScanTriggerPressed()
                }
            )

            if (showManualInput) {
                ManualEntryDialog(
                    onDismiss = { showManualInput = false },
                    onConfirm = { code ->
                        viewModel.onManualCodeEntered(code)
                        showManualInput = false
                    }
                )
            }
        }
    }
}

@Composable
fun ManualEntryDialog(onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    val inputState = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.scanner_manual_entry_hint)) },
        text = {
            OutlinedTextField(
                value = inputState.value,
                onValueChange = { input: String ->
                    inputState.value = input
                },
                label = { Text(stringResource(R.string.input_label_code)) },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(inputState.value) }) {
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
                contentDescription = null
            )
        }

        Row(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = onManualEntry) {
                Icon(Icons.Default.Edit, null)
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
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
    }
}
