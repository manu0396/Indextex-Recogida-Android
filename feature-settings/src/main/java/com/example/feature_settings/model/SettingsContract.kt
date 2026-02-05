package com.example.feature_settings.model

data class SettingsUiState(
    val isLoading: Boolean = false,
    val syncStatus: SyncStatus = SyncStatus.Idle,
    val lastSync: String = "04 Febrero 2026 12:38:00",
    val center: String = "arteixozara",
    val trayecto: String = "Automático",
    val mode: String = "Seguridad",
    val environment: String = "Preproduccion",
    val appVersion: String = "2.8.0",
    val userEmail: String? = null
)

sealed interface SettingsUiEvent {
    data object OnBackClicked : SettingsUiEvent
    data object OnLogoutClicked : SettingsUiEvent
    data object OnThemeToggled : SettingsUiEvent
    data object OnSyncClicked : SettingsUiEvent
}

sealed interface SyncStatus {
    data object Idle : SyncStatus
    data object Loading : SyncStatus
    data class Success(val message: String) : SyncStatus
    data class Error(val message: String) : SyncStatus
}
