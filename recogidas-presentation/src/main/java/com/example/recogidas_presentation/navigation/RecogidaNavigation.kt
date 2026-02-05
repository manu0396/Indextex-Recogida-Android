package com.example.recogidas_presentation.navigation

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.recogidas_presentation.ui.screens.ManualEntryScreen
import com.example.recogidas_presentation.ui.screens.ScannerScreen
import com.example.recogidas_presentation.viewmodel.ScannerViewModel
import org.koin.androidx.compose.koinViewModel

const val ROUTE_RECOGIDA_SCANNER = "recogida_scanner"
const val ROUTE_MANUAL_ENTRY = "manual_entry"
const val KEY_MANUAL_CODE = "manual_code_result"

private val TAG = "Navigation"

fun NavGraphBuilder.recogidaGraph(
    navController: NavController,
    onSettingsClick: () -> Unit,
) {
    composable(route = ROUTE_RECOGIDA_SCANNER) { backStackEntry ->
        val viewModel: ScannerViewModel = koinViewModel()
        val manualResult = backStackEntry.savedStateHandle.get<String>(KEY_MANUAL_CODE)

        LaunchedEffect(manualResult) {
            manualResult?.let { code ->
                Log.d(TAG, "Código recibido de manual: $code")
                backStackEntry.savedStateHandle.remove<String>(KEY_MANUAL_CODE)
            }
        }

        ScannerScreen(
            viewModel = viewModel,
            onSettingsClick = onSettingsClick
        )
    }

    composable(route = ROUTE_MANUAL_ENTRY) {
        ManualEntryScreen(
            onBack = { navController.popBackStack() },
            onCodeSubmitted = { code ->
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(KEY_MANUAL_CODE, code)
                navController.popBackStack()
            }
        )
    }
}
