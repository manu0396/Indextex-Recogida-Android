package com.example.recogidas_presentation.viewmodel

import com.example.domain.model.ScanResult
import com.example.domain.usecases.ValidateQrUseCase
import com.example.recogidas_presentation.ui.screens.states.ScannerUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ScannerViewModel(
    private val validateQrUseCase: ValidateQrUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState: StateFlow<ScannerUiState> = _uiState.asStateFlow()

    private var lastTriggerTime = 0L

    fun onScanTriggerPressed() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTriggerTime < 500) return
        lastTriggerTime = currentTime

        if (_uiState.value.isScanPending) stopScanning() else startScanning()
    }

    fun startScanning() {
        _uiState.update {
            it.copy(
                isScanning = true,
                isScanPending = true,
                scanStatus = ScanResult.Idle,
                lastScannedCode = null
            )
        }
    }

    fun stopScanning() {
        _uiState.update {
            it.copy(
                isScanning = false,
                isScanPending = false,
                isProcessing = false
            )
        }
    }

    fun toggleTorch() {
        _uiState.update { it.copy(isTorchOn = !it.isTorchOn) }
    }

    fun onManualCodeEntered(code: String) {
        if (code.isNotBlank()) {
            validateCode(code.trim())
        }
    }

    fun onCodeScanned(code: String) {
        val currentState = _uiState.value
        if (!currentState.isScanPending || currentState.isProcessing) return
        if (currentState.scanStatus !is ScanResult.Idle) return
        validateCode(code)
    }

    private fun validateCode(code: String) {
        launchSafe(
            onLoading = { loadingState ->
                _uiState.update { it.copy(isProcessing = loadingState) }
            },
            onError = { errorMsg ->
                _uiState.update { it.copy(scanStatus = ScanResult.Error(errorMsg)) }
                resetScanStateAfterDelay()
            }
        ) {
            _uiState.update { it.copy(lastScannedCode = code) }
            validateQrUseCase(code).collect { result ->
                _uiState.update { currentState ->
                    currentState.copy(
                        scanStatus = result,
                        isScanPending = when(result) {
                            is ScanResult.Success -> false
                            is ScanResult.Error,
                            is ScanResult.FormatError -> false
                            else -> currentState.isScanPending
                        }
                    )
                }
                if (result is ScanResult.Success ||
                    result is ScanResult.Error ||
                    result is ScanResult.FormatError) {
                    resetScanStateAfterDelay()
                }
            }
        }
    }

    private fun resetScanStateAfterDelay() {
        launchSafe {
            delay(2000)
            _uiState.update {
                it.copy(
                    scanStatus = ScanResult.Idle,
                    lastScannedCode = null,
                    isProcessing = false,
                    isScanPending = it.isScanning
                )
            }
        }
    }
}
