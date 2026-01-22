package com.example.recogidas_presentation.ui.screens

import androidx.compose.runtime.Immutable

enum class ScanMode {
    NORMAL,
    RECOVERY,
    INVENTORY
}

@Immutable
data class RecogidaUiState(
    val isLoading: Boolean = false,
    val name: String = "",
    val type: String = "",
    val error: String? = null,
    val isLaserActive: Boolean = true,
    val lastScanTimestamp: Long = 0L,
    val mode: ScanMode = ScanMode.NORMAL,
    val centerId: String = "",
    val frontCameraActive: Boolean = false
)

sealed class RecogidasIntent {
    data object InitScanner : RecogidasIntent()
    data object SyncData : RecogidasIntent()
    data class ScanDetected(val qr: String) : RecogidasIntent()
    data object RefreshData : RecogidasIntent()
    data class ToggleLaser(val enabled: Boolean) : RecogidasIntent()
    data object ToggleCamera : RecogidasIntent()
    data class ChangeMode(val mode: ScanMode) : RecogidasIntent()
}
