package com.example.domain

import com.example.core_common.dispatchers.DispatcherProvider
import com.example.domain.repository.RecogidasRepository
import com.example.domain.usecases.SyncAuthorizedUserUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SyncAuthorizedUserUseCaseTest {
    private val testDispatcher = StandardTestDispatcher()
    private val mockRepository: RecogidasRepository = mockk()

    private val testDispatchers = object : DispatcherProvider {
        override val main = testDispatcher
        override val io = testDispatcher
        override val default = testDispatcher
    }

    private val useCase = SyncAuthorizedUserUseCase(
        repository = mockRepository,
        dispatchers = testDispatchers
    )

    @Test
    fun `when sync is triggered then repository is called`() = runTest(testDispatcher) {
        coEvery { mockRepository.syncAuthorizedPersonnel() } returns Unit
        useCase.invoke()
        coVerify(exactly = 1) { mockRepository.syncAuthorizedPersonnel() }
    }
}
