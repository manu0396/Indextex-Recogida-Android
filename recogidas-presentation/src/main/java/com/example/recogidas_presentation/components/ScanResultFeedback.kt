package com.example.recogidas_presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.ScanResult
import com.example.recogidas_presentation.ui.screens.mapper.ScanResultMapper

@Composable
fun ScanResultFeedback(
    result: ScanResult,
    modifier: Modifier = Modifier,
    mapper: ScanResultMapper = remember { ScanResultMapper() }
) {
    val isVisible by remember(result) {
        derivedStateOf { result !is ScanResult.Idle }
    }

    val uiModel = mapper.map(result)

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + expandVertically(animationSpec = tween(300, easing = FastOutSlowInEasing)),
            exit = fadeOut() + shrinkVertically(animationSpec = tween(200)),
            label = "ScanFeedbackAnimation"
        ) {
            if (result is ScanResult.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                uiModel?.let { model ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = model.color.copy(alpha = 0.95f)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(0.85f)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = model.icon,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(72.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = model.title,
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = model.description,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White.copy(alpha = 0.9f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Success State")
@Composable
fun PreviewScanSuccess() {
    MaterialTheme {
        Surface(modifier = Modifier.height(300.dp).fillMaxWidth()) {
            ScanResultFeedback(
                result = ScanResult.Success(
                    name = "Example Item",
                    type = "Standard"
                )
            )
        }
    }
}
