package com.example.recogidas_presentation

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertIsDisplayed
import com.example.recogidas_presentation.components.ErrorView
import org.junit.Rule
import org.junit.Test

class RecogidasTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `when state is error, retry button is displayed and clickable`() {
        var onRetryCalled = false

        composeTestRule.setContent {
            ErrorView(
                message = "Connection Error",
                onRetry = { onRetryCalled = true }
            )
        }

        composeTestRule.onNodeWithText("Reintentar").assertIsDisplayed().performClick()
        assert(onRetryCalled)
    }
}
