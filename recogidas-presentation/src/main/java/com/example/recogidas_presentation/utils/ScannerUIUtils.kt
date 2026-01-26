package com.example.recogidas_presentation.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.example.domain.model.ScanResult
import com.example.recogidas_presentation.R

/**
 * Single source of truth for Scanner UI Constants and Mapping logic.
 */
object ScannerUIConstants {
    val SuccessGreen = Color(0xFF4CAF50)
    const val VALIDATION_DELAY = 400L
}

data class ScanFeedbackUiModel(
    val icon: ImageVector,
    val color: Color,
    val title: String,
    val description: String
)

