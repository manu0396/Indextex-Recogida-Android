package com.example.recogidas_presentation

import app.cash.turbine.test
import com.example.domain.usecases.SyncAuthorizedUserUseCase
import com.example.domain.usecases.ValidateQrUseCase
import com.example.recogidas_presentation.ui.screens.RecogidasIntent
import com.example.recogidas_presentation.viewmodel.RecogidasViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RecogidasViewModelTest {
    private val syncUseCase: SyncAuthorizedUserUseCase = mockk()
    private val validateQrUseCase: ValidateQrUseCase = mockk()
    private lateinit var viewModel: RecogidasViewModel

    @Test
    fun `initial state should be loading false`() = runTest {
        viewModel = RecogidasViewModel(validateQrUseCase, syncUseCase)
        viewModel.uiState.test {
            val initialState = awaitItem()
            assert(!initialState.isLoading)
        }
    }

    @Test
    fun `when SyncData intent is sent, loading state should toggle`() = runTest {
        coEvery { syncUseCase() } returns Result.success(Unit)
        viewModel = RecogidasViewModel(validateQrUseCase, syncUseCase)

        viewModel.uiState.test {
            viewModel.onIntent(RecogidasIntent.SyncData)
            assert(awaitItem().isLoading)
            assert(!awaitItem().isLoading)
        }
    }
}
