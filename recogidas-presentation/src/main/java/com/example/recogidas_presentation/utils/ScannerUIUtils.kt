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
}

data class ScanFeedbackUiModel(
    val icon: ImageVector,
    val color: Color,
    val title: String,
    val description: String
)

/**
 * Mapper to convert Domain Result into UI Model.
 * MUST be Composable because it accesses MaterialTheme and String Resources.
 */
@Composable
fun ScanResult.toUiModel(): ScanFeedbackUiModel? {
    return when (this) {
        is ScanResult.Success -> ScanFeedbackUiModel(
            icon = Icons.Rounded.CheckCircle,
            color = ScannerUIConstants.SuccessGreen,
            title = stringResource(R.string.scanner_status_validated),
            description = this.name
        )
        is ScanResult.Error -> ScanFeedbackUiModel(
            icon = Icons.Rounded.Error,
            color = MaterialTheme.colorScheme.error,
            title = stringResource(R.string.scanner_status_error),
            description = this.message
        )
        else -> null
    }
}
