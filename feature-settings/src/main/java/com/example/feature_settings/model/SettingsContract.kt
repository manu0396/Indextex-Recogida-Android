package com.example.feature_settings.model

data class SettingsUiState(
    val isLoading: Boolean = false,
    val appVersion: String = "",
    val environment: String = "",
    val userEmail: String? = null
)

sealed interface SettingsUiEvent {
    data object OnBackClicked : SettingsUiEvent
    data object OnLogoutClicked : SettingsUiEvent
    data object OnThemeToggled : SettingsUiEvent
}
