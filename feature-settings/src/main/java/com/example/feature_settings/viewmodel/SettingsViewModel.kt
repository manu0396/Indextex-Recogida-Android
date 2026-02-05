package com.example.feature_settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_common.dispatchers.result.Result
import com.example.domain.usecases.GetSessionInfoUseCase
import com.example.domain.usecases.LogoutUseCase
import com.example.feature_settings.model.SettingsUiEvent
import com.example.feature_settings.model.SettingsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

            // Executing UseCase (Pattern based on your SyncAuthorizedUserUseCase)
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
                    // Handle error (e.g., show generic info or retry)
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
            SettingsUiEvent.OnBackClicked -> {
                // Usually handled by UI/Navigation, but can emit a side-effect here
            }
            SettingsUiEvent.OnLogoutClicked -> logout()
            SettingsUiEvent.OnThemeToggled -> {
                // Toggle theme logic
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (logoutUseCase.run(Unit)) {
                is Result.Success -> {
                    // Navigate to Login (Handled by UI observing state or specific side-effect)
                    _uiState.update { it.copy(isLoading = false) }
                }
                is Result.Error -> {
                    // Handle logout failure
                    _uiState.update { it.copy(isLoading = false) }
                }
                is Result.Loading -> _uiState.update { it.copy(isLoading = true) }
            }
        }
    }
}
