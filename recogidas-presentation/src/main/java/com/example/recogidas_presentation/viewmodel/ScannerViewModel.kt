package com.example.recogidas_presentation.viewmodel

import android.util.Log
import com.example.domain.model.ScanResult
import com.example.domain.usecases.ValidateQrUseCase
import com.example.recogidas_presentation.ui.screens.states.ScannerStage
import com.example.recogidas_presentation.ui.screens.states.ScannerUiState
import com.example.recogidas_presentation.utils.ScannerUIConstants
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ScannerViewModel(
    private val validateQrUseCase: ValidateQrUseCase,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ScannerUiState(
        isScanning = true,
        isScanPending = true,
        stage = ScannerStage.DETECTING
    ))
    val uiState: StateFlow<ScannerUiState> = _uiState.asStateFlow()

    // Fixed: Restored and synchronized with the new stage logic
    fun onScanTriggerPressed() {
        _uiState.update { state ->
            val nextPending = !state.isScanPending
            state.copy(
                isScanPending = nextPending,
                stage = if (nextPending) ScannerStage.DETECTING else ScannerStage.IDLE
            )
        }
    }

    fun toggleTorch() {
        _uiState.update { it.copy(isTorchOn = !it.isTorchOn) }
    }

    fun onManualCodeEntered(code: String) {
        if (code.isBlank() || _uiState.value.isProcessing) return
        val sanitized = code.trim().uppercase()

        _uiState.update { it.copy(
            stage = ScannerStage.VALIDATING,
            isProcessing = true,
            isScanPending = false,
            lastScannedCode = sanitized,
            scanStatus = ScanResult.Loading
        )}

        validateCode(sanitized)
    }

    fun onCodeScanned(code: String) {
        val currentState = _uiState.value
        if (currentState.stage != ScannerStage.DETECTING || !currentState.isScanPending) return
        val sanitizedCode = code
            .filter { it.isLetterOrDigit() || it in "_" }
            .trim()
            .uppercase()
        Log.d("ScannerVM", "Raw: $code -> Sanitized: $sanitizedCode")
        _uiState.update { state ->
            state.copy(
                stage = ScannerStage.VALIDATING,
                isScanPending = false,
                isProcessing = true,
                lastScannedCode = sanitizedCode
            )
        }
        validateCode(sanitizedCode)
    }

    private fun validateCode(code: String) {
        launchSafe(
            onLoading = { isLoading ->
                _uiState.update { it.copy(
                    isProcessing = isLoading,
                    isScanPending = if (isLoading) false else it.isScanPending,
                    stage = if (isLoading) ScannerStage.VALIDATING else it.stage
                ) }
            },
            onError = { msg -> updateStage(ScanResult.Error(msg)) }
        ) {
            validateQrUseCase(code).collect { result ->
                if (result !is ScanResult.Loading) {
                    delay(ScannerUIConstants.VALIDATION_DELAY)
                    updateStage(result)
                }
            }
        }
    }

    private fun updateStage(result: ScanResult) {
        _uiState.update { it.copy(
            scanStatus = result,
            isProcessing = false,
            stage = ScannerStage.FEEDBACK
        ) }

        launchSafe {
            // Auto-reset logic
            delay(if (result is ScanResult.Success) 1500L else 2500L)
            _uiState.update { it.copy(
                scanStatus = ScanResult.Idle,
                stage = ScannerStage.DETECTING,
                isScanPending = true
            ) }
        }
    }
}
