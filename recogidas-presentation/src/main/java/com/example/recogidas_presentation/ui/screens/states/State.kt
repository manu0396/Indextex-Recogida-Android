package com.example.recogidas_presentation.ui.screens.states

import com.example.domain.model.ScanResult

data class RecogidasUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isCameraActive: Boolean = true
)

sealed class RecogidasEvent {
    data object Retry : RecogidasEvent()
    data class ScanCode(val qr: String) : RecogidasEvent()
}

data class ScannerUiState(
    val isScanning: Boolean = false,
    val isTorchOn: Boolean = false,
    val lastScannedCode: String? = null,
    val scanStatus: ScanResult = ScanResult.Idle,
    val isProcessing: Boolean = false,
    val isScanPending: Boolean = false
)
