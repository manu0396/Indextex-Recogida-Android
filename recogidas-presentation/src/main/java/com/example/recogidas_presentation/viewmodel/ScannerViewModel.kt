package com.example.recogidas_presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.domain.model.ScanResult
import com.example.domain.repository.RecogidasRepository
import com.example.recogidas_presentation.ui.screens.states.ScannerUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScannerViewModel(
    private val repository: RecogidasRepository,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState: StateFlow<ScannerUiState> = _uiState.asStateFlow()

    private var lastTriggerTime = 0L

    fun toggleTorch() {
        _uiState.update { it.copy(isTorchOn = !it.isTorchOn) }
    }

    private fun isValidCode(code: String): Boolean {
        val regex = Regex("^INDITEX-\\d{6}$")
        return regex.matches(code)
    }

    fun onScanTriggerPressed() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTriggerTime < 500) return
        lastTriggerTime = currentTime
        resetScanState()
        _uiState.update {
            it.copy(
                isScanPending = true,
                scanStatus = ScanResult.Idle
            )
        }
    }

    fun onQrCodeScanned(code: String) {
        val currentState = _uiState.value
        if (currentState.isProcessing || !currentState.isScanPending) return

        if (!isValidCode(code)) {
            _uiState.update {
                it.copy(
                    scanStatus = ScanResult.Error("El código debe empezar por INDITEX- seguido de 6 números"),
                    isScanPending = false
                )
            }
            resetScanStateAfterDelay()
            return
        }

        launchSafe(
            onLoading = { isLoading ->
                _uiState.update { it.copy(isProcessing = isLoading) }
            },
            onError = { errorMsg ->
                _uiState.update { it.copy(scanStatus = ScanResult.Error(errorMsg)) }
                resetScanStateAfterDelay()
            }
        ) {
            repository.validateQr(code).collect { result ->
                _uiState.update {
                    it.copy(
                        scanStatus = result,
                        isScanPending = false,
                        lastScannedCode = if (result is ScanResult.Success) code else null
                    )
                }
                if (result is ScanResult.Success || result is ScanResult.Error) {
                    resetScanStateAfterDelay()
                }
            }
        }
    }

    private fun resetScanStateAfterDelay() {
        viewModelScope.launch {
            delay(2000)
            resetScanState()
        }
    }

    fun resetScanState() {
        _uiState.update {
            it.copy(
                scanStatus = ScanResult.Idle,
                lastScannedCode = null,
                isProcessing = false,
                isScanPending = false
            )
        }
    }
}
