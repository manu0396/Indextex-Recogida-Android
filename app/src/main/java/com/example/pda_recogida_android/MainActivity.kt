package com.example.pda_recogida_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.recogidas_presentation.navigation.ROUTE_RECOGIDA_SCANNER
import com.example.recogidas_presentation.navigation.recogidaGraph
import com.example.recogidas_presentation.ui.theme.InditexRecogidaAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InditexRecogidaAndroidTheme() {
                MainAppNavigation()
            }
        }
    }
}

@Composable
fun MainAppNavigation() {
    val navController = rememberNavController()

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ROUTE_RECOGIDA_SCANNER,
            modifier = Modifier.padding(innerPadding)
        ) {
            recogidaGraph(navController)
        }
    }
}
