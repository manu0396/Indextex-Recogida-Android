package com.example.recogidas_presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.ScanResult
import com.example.domain.repository.RecogidasRepository
import com.example.recogidas_presentation.R
import com.example.recogidas_presentation.ui.screens.states.ScannerUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScannerViewModel(
    private val repository: RecogidasRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState: StateFlow<ScannerUiState> = _uiState.asStateFlow()

    private var lastTriggerTime = 0L

    fun toggleTorch() {
        _uiState.update { it.copy(isTorchOn = !it.isTorchOn) }
    }

    fun stopScanning() {
        _uiState.update {
            it.copy(
                isScanPending = false,
                scanStatus = ScanResult.Idle,
                isProcessing = false
            )
        }
    }

    private fun isValidCode(code: String): Boolean {
        return code.matches(Regex("^INDITEX-\\d{6}$"))
    }

    fun onScanTriggerPressed() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTriggerTime < 500) return
        lastTriggerTime = currentTime

        _uiState.update {
            it.copy(
                isScanPending = true,
                scanStatus = ScanResult.Idle,
                lastScannedCode = null,
                isProcessing = false
            )
        }
    }

    fun onQrCodeScanned(code: String) {
        if (_uiState.value.isProcessing || !_uiState.value.isScanPending) return
        processCode(code)
    }

    fun onManualCodeScanned(code: String) {
        if (_uiState.value.isProcessing) return
        processCode(code)
    }

    private fun processCode(code: String) {
        if (!isValidCode(code)) {
            val errorMsg = getApplication<Application>().getString(R.string.error_invalid_format_msg)
            _uiState.update {
                it.copy(scanStatus = ScanResult.Error(errorMsg), isScanPending = false)
            }
            resetScanStateAfterDelay()
            return
        }

        _uiState.update { it.copy(isProcessing = true) }

        viewModelScope.launch {
            try {
                repository.validateQr(code).collect { result ->
                    _uiState.update {
                        it.copy(
                            scanStatus = result,
                            isScanPending = false,
                            isProcessing = false,
                            lastScannedCode = if (result is ScanResult.Success) code else null
                        )
                    }
                    if (result is ScanResult.Success || result is ScanResult.Error) {
                        resetScanStateAfterDelay()
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(scanStatus = ScanResult.Error(e.message ?: "Error"), isProcessing = false)
                }
                resetScanStateAfterDelay()
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
            it.copy(scanStatus = ScanResult.Idle, lastScannedCode = null, isProcessing = false, isScanPending = false)
        }
    }
}
