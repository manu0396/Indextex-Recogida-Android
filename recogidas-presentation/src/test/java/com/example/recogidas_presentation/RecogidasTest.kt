package com.example.recogidas_presentation

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.recogidas_presentation.components.ErrorView
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
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
        composeTestRule.waitForIdle()
        composeTestRule
            .onNodeWithText("Reintentar", substring = true, ignoreCase = true, useUnmergedTree = true)
            .assertExists()
            .performClick()
        assert(onRetryCalled)
    }
}
