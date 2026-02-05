package com.example.pda_recogida_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.feature_settings.navigation.settingsScreen
import com.example.recogidas_presentation.navigation.ROUTE_RECOGIDA_SCANNER
import com.example.recogidas_presentation.navigation.recogidaGraph
import com.example.pda_recogida_android.ui.theme.InditexRecogidaAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InditexRecogidaAndroidTheme {
                val navController = rememberNavController()

                Scaffold { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = ROUTE_RECOGIDA_SCANNER,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // 1. Scanner Graph
                        recogidaGraph(navController)

                        // 2. Settings Graph
                        settingsScreen(
                            onBackClick = { navController.popBackStack() },
                            onLogoutSuccess = {
                                // Navigate back to scanner and clear backstack
                                navController.navigate(ROUTE_RECOGIDA_SCANNER) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
