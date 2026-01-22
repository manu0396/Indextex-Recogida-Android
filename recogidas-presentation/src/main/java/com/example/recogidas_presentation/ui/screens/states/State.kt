package com.example.recogidas_presentation.ui.screens.states

data class RecogidasUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isCameraActive: Boolean = true
)

sealed class RecogidasEvent {
    data object Retry : RecogidasEvent()
    data class ScanCode(val qr: String) : RecogidasEvent()
}
