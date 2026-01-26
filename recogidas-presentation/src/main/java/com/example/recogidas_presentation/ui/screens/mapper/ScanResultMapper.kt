package com.example.recogidas_presentation.ui.screens.mapper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.domain.model.ScanResult
import com.example.recogidas_presentation.R
import com.example.recogidas_presentation.utils.ScanFeedbackUiModel
import com.example.recogidas_presentation.utils.ScannerUIConstants

class ScanResultMapper {
    @Composable
    fun map(result: ScanResult): ScanFeedbackUiModel? {
        return when (result) {
            is ScanResult.Success -> ScanFeedbackUiModel(
                icon = Icons.Rounded.CheckCircle,
                color = ScannerUIConstants.SuccessGreen,
                title = stringResource(R.string.scanner_status_validated),
                description = result.name
            )
            is ScanResult.Error -> ScanFeedbackUiModel(
                icon = Icons.Rounded.Error,
                color = MaterialTheme.colorScheme.error,
                title = stringResource(R.string.scanner_status_error),
                description = result.message
            )
            is ScanResult.FormatError -> ScanFeedbackUiModel(
                icon = Icons.Rounded.Block,
                color = MaterialTheme.colorScheme.error,
                title = stringResource(R.string.scanner_status_format_error),
                description = stringResource(
                    R.string.scanner_error_format_details,
                    result.read,
                    result.expected
                )
            )
            else -> null
        }
    }
}
