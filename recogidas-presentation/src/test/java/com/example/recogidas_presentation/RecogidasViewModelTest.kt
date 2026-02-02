package com.example.recogidas_presentation

import app.cash.turbine.test
import com.example.core_common.dispatchers.result.Result
import com.example.domain.usecases.SyncAuthorizedUserUseCase
import com.example.domain.usecases.ValidateQrUseCase
import com.example.recogidas_presentation.ui.screens.RecogidasIntent
import com.example.recogidas_presentation.viewmodel.RecogidasViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecogidasViewModelTest {
    private val syncUseCase: SyncAuthorizedUserUseCase = mockk()
    private val validateQrUseCase: ValidateQrUseCase = mockk()
    private lateinit var viewModel: RecogidasViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RecogidasViewModel(validateQrUseCase, syncUseCase)
    }

    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `when RefreshData intent is sent then state updates to success`() = runTest {
        coEvery { syncUseCase(Unit) } returns Result.Success(Unit)

        viewModel.uiState.test {
            val initialState = awaitItem()
            assert(!initialState.isLoading)
            viewModel.onIntent(RecogidasIntent.RefreshData)
            runCurrent()
            advanceUntilIdle()
            val finalState = expectMostRecentItem()
            assert(!finalState.isLoading)
            assert(finalState.error == null)
        }
    }
}
