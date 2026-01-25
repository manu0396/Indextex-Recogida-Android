package com.example.recogidas_presentation.viewmodel

import androidx.camera.core.CameraSelector
import com.example.domain.model.ScanResult
import com.example.domain.usecases.SyncAuthorizedUserUseCase
import com.example.domain.usecases.ValidateQrUseCase
import com.example.recogidas_presentation.ui.screens.RecogidaUiState
import com.example.recogidas_presentation.ui.screens.RecogidasIntent
import com.example.recogidas_presentation.ui.screens.ScanMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RecogidasViewModel(
    private val validateQrUseCase: ValidateQrUseCase,
    private val syncUseCase: SyncAuthorizedUserUseCase,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(RecogidaUiState())
    val uiState = _uiState.asStateFlow()

    override fun handleError(t: Throwable) {
        _uiState.update {
            it.copy(
                isLoading = false,
                error = t.message ?: "Unknown Error"
            )
        }
    }

    fun onIntent(intent: RecogidasIntent) {
        when (intent) {
            is RecogidasIntent.InitScanner -> handleInit()
            is RecogidasIntent.ScanDetected -> processScan(intent.qr)
            is RecogidasIntent.RefreshData -> syncData()
            is RecogidasIntent.ToggleLaser -> handleToggleLaser(intent.enabled)
            is RecogidasIntent.ToggleCamera -> toggleCamera()
            is RecogidasIntent.ChangeMode -> updateScanMode(intent.mode)
            is RecogidasIntent.SyncData -> syncData()
        }
    }

    private fun toggleCamera() {
        _uiState.update { it.copy(frontCameraActive = !it.frontCameraActive) }
    }

    private fun handleInit() {
        _uiState.update { it.copy(isLaserActive = true, error = null) }
    }

    private fun processScan(qr: String) = launchSafe(
        onLoading = { isLoading ->
            _uiState.update { it.copy(isLoading = isLoading) }
        },
        onError = { errorMsg ->
            _uiState.update { it.copy(error = errorMsg) }
        }
    ) {
        validateQrUseCase(qr).collect { result: ScanResult ->
            _uiState.update { currentState ->
                when (result) {
                    is ScanResult.Loading -> {
                        currentState.copy(isLoading = true, error = null)
                    }
                    is ScanResult.Success -> {
                        currentState.copy(
                            isLoading = false,
                            name = result.name,
                            type = result.type,
                            error = null,
                            lastScanTimestamp = System.currentTimeMillis()
                        )
                    }
                    is ScanResult.FormatError -> {
                        currentState.copy(
                            isLoading = false,
                            error = "Formato incorrecto. Leído: '${result.read}'. Se requiere ${result.expected}",
                            name = "",
                            type = ""
                        )
                    }
                    is ScanResult.Error -> {
                        currentState.copy(
                            isLoading = false,
                            error = result.message,
                            name = "",
                            type = ""
                        )
                    }
                    else -> currentState
                }
            }
        }
    }

    private fun syncData() =
        launchSafe(
            onLoading = { loadingState ->
                _uiState.update { it.copy(isLoading = loadingState) }
            },
            onError = { message ->
                _uiState.update { it.copy(error = message) }
            }
        ) {
            syncUseCase()
        }

    private fun handleToggleLaser(enabled: Boolean) {
        _uiState.update { it.copy(isLaserActive = enabled) }
    }

    fun getCameraSelector(): CameraSelector {
        return if (uiState.value.frontCameraActive) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    fun updateScanMode(newMode: ScanMode) {
        _uiState.update { it.copy(mode = newMode) }
    }
}
