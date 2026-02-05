package com.example.feature_settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_common.dispatchers.result.Result
import com.example.domain.usecases.GetSessionInfoUseCase
import com.example.domain.usecases.LogoutUseCase
import com.example.feature_settings.model.SettingsUiEvent
import com.example.feature_settings.model.SettingsUiState
import com.example.feature_settings.model.SyncStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class SettingsViewModel(
    private val getSessionInfoUseCase: GetSessionInfoUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = getSessionInfoUseCase.run(Unit)) {
                is Result.Success -> {
                    val info = result.data
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            appVersion = info.appVersion,
                            environment = info.environment,
                            userEmail = info.userEmail
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userEmail = "Unknown"
                        )
                    }
                }
                Result.Loading -> _uiState.update { it.copy(isLoading = true) }
            }
        }
    }

    fun onEvent(event: SettingsUiEvent) {
        when (event) {
            SettingsUiEvent.OnBackClicked -> { /* Handle navigation side-effect */
            }
            SettingsUiEvent.OnLogoutClicked -> logout()
            SettingsUiEvent.OnThemeToggled -> { /* Toggle logic */
            }
            SettingsUiEvent.OnSyncClicked -> triggerMockSync()
        }
    }

    private fun triggerMockSync() {
        viewModelScope.launch {
            _uiState.update { it.copy(syncStatus = SyncStatus.Loading) }

            // 🟢 Simulate Network Latency
            delay(2000)

            // 🟢 Randomly Mock Success or Failure
            if (Random.nextBoolean()) {
                _uiState.update {
                    it.copy(
                        syncStatus = SyncStatus.Success("Sincronización finalizada"),
                        lastSync = "05 Febrero 2026 11:35:00" // Update to "current" time
                    )
                }
            } else {
                _uiState.update {
                    it.copy(syncStatus = SyncStatus.Error("Error de conexión con el servidor"))
                }
            }

            // Optional: Reset to Idle after showing the message for a few seconds
            delay(3000)
            _uiState.update { it.copy(syncStatus = SyncStatus.Idle) }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (logoutUseCase.run(Unit)) {
                is Result.Success -> {
                    _uiState.update { it.copy(
                        isLoading = false,
                        userEmail = null
                    ) }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
                is Result.Loading -> _uiState.update { it.copy(isLoading = true) }
            }
        }
    }
}
