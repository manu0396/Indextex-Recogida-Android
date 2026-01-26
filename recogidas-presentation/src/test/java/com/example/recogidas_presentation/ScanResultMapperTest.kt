package com.example.recogidas_presentation

import androidx.compose.ui.test.junit4.createComposeRule
import com.example.domain.model.ScanResult
import com.example.recogidas_presentation.ui.screens.mapper.ScanResultMapper
import com.example.recogidas_presentation.utils.ScannerUIConstants
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ScanResultMapperTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mapper = ScanResultMapper()

    @Test
    fun `map Success returns UI model with success green and name`() {
        val domainResult = ScanResult.Success(name = "Object A", type = "Standard")

        composeTestRule.setContent {
            val uiModel = mapper.map(domainResult)

            assertEquals(ScannerUIConstants.SuccessGreen, uiModel?.color)
            assertEquals("Object A", uiModel?.description)
        }
    }

    @Test
    fun `map Error returns UI model with error color and message`() {
        val errorMessage = "Network Failure"
        val domainResult = ScanResult.Error(message = errorMessage)

        composeTestRule.setContent {
            val uiModel = mapper.map(domainResult)
            assertEquals(errorMessage, uiModel?.description)
        }
    }

    @Test
    fun `map FormatError returns UI model with formatted details`() {
        val read = "URL:123"
        val expected = "INDITEX_XXXXXX"
        val domainResult = ScanResult.FormatError(read = read, expected = expected)

        composeTestRule.setContent {
            val uiModel = mapper.map(domainResult)
            assert(uiModel?.description?.contains(read) == true)
            assert(uiModel?.description?.contains(expected) == true)
        }
    }

    @Test
    fun `map Idle returns null`() {
        val domainResult = ScanResult.Idle
        composeTestRule.setContent {
            val uiModel = mapper.map(domainResult)
            assertNull(uiModel)
        }
    }
}
