package com.example.data

import com.example.core_common.dispatchers.DispatcherProvider
import com.example.data.repository.RecogidasRepositoryImpl
import com.example.data.datasources.RecogidasLocalDataSource
import com.example.data.datasources.RecogidasRemoteDataSource
import com.example.data.models.RecogidaResponse
import com.example.domain.repository.RecogidasRepository
import com.example.data.mapper.RecogidaMapper
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RecogidasRepositoryImplTest {
    private val localDataSource: RecogidasLocalDataSource = mockk()
    private val remoteDataSource: RecogidasRemoteDataSource = mockk()
    private val mapper: RecogidaMapper = RecogidaMapper()
    private val dispatchers: DispatcherProvider = mockk()

    private lateinit var repository: RecogidasRepository

    @Before
    fun setup() {
        val testDispatcher = UnconfinedTestDispatcher()
        every { dispatchers.io } returns testDispatcher
        every { dispatchers.main } returns testDispatcher
        every { dispatchers.default } returns testDispatcher

        repository = RecogidasRepositoryImpl(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            mapper = mapper,
            dispatchers = dispatchers
        )
    }

    @Test
    fun `when sync succeeds, database stores full_name and role_type`() = runTest {
        // GIVEN
        val mockResponse = listOf(
            RecogidaResponse(
                id = "USR-99",
                fullName = "Manuel Lucas",
                roleType = "ADMIN",
                timestamp = 1769079870924L
            )
        )

        coEvery { remoteDataSource.getAuthorizedPersonnel() } returns mockResponse
        coEvery { localDataSource.clearAndSaveAll(any()) } just Runs
        repository.syncAuthorizedPersonnel()
        coVerify {
            localDataSource.clearAndSaveAll(match { list ->
                list.size == 1 && list[0].qrCode == "USR-99"
            })
        }
    }
}
