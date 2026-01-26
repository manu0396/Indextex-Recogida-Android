package com.example.recogidas_presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.recogidas_presentation.components.ValidationSuccessDialog
import com.example.recogidas_presentation.components.ValidationErrorDialog
import com.example.recogidas_presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualEntryScreen(
    onBack: () -> Unit,
    onCodeSubmitted: (String) -> Unit
) {
    var codeInput by remember { mutableStateOf(TextFieldValue("")) }

    fun validateAndSubmit() {
        val text = codeInput.text.trim().uppercase()
        if (text.isNotBlank()) {
            onCodeSubmitted(text)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.scanner_manual_entry_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = codeInput,
                onValueChange = { codeInput = it },
                placeholder = { Text(stringResource(R.string.scanner_code_hint)) },
                label = { Text(stringResource(R.string.scanner_code_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                // Removed: isError = showErrorDialog
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { validateAndSubmit() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = codeInput.text.isNotBlank()
            ) {
                Text(stringResource(R.string.scanner_validate_code_button))
            }
        }
    }
}
