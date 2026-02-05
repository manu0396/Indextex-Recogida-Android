package com.example.feature_settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.feature_settings.ui.screens.SettingsRoute

const val SETTINGS_ROUTE = "settings_route"

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    this.navigate(SETTINGS_ROUTE, navOptions)
}

fun NavGraphBuilder.settingsScreen(
    onBackClick: () -> Unit,
    onLogoutSuccess: () -> Unit
) {
    composable(route = SETTINGS_ROUTE) {
        SettingsRoute(
            onBackClick = onBackClick,
            onLogoutSuccess = onLogoutSuccess
        )
    }
}
