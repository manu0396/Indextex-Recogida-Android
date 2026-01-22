package com.example.recogidas_presentation.navigation

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.recogidas_presentation.ui.screens.RecogidaScannerScreen

const val ROUTE_RECOGIDA_SCANNER = "recogida_scanner"

private val TAG = "Navigation"
fun NavGraphBuilder.recogidaGraph(navController: NavController) {
    composable(route = ROUTE_RECOGIDA_SCANNER) {
        val currentEntry by navController.currentBackStackEntryAsState()
        val canNavigateBack = currentEntry != null && navController.previousBackStackEntry != null
        RecogidaScannerScreen(
            modifier = Modifier.fillMaxSize(),
            onBack = if (canNavigateBack) {
                {
                    navController.popBackStack()
                }
            } else {
                Log.e(TAG, "No backstack entry found")
                null
            }
        )
    }
}
