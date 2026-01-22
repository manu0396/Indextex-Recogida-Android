package com.example.data

import com.example.data.api.RecogidasApi
import com.example.data.db.AuthorizedEntity
import com.example.data.models.RecogidaResponse
import com.example.domain.repository.RecogidasRepository
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Test
import com.example.data.db.RecogidaQueries

class RecogidasRepositoryImplTest {
    private val api: RecogidasApi = mockk()
    private val queries: RecogidaQueries = mockk(relaxed = true)
    private lateinit var repository: RecogidasRepository

    @Test
    fun `when sync succeeds, database stores full_name and role_type`() = runTest {
        val mockResponse = listOf(
            RecogidaResponse(
                id = "USR-99",
                fullName = "Manuel Lucas",
                roleType = "ADMIN",
                timestamp = 1769079870924L
            )
        )
        coEvery { api.getAuthorizedPersonnel() } returns mockResponse
        repository.syncAuthorizedPersonnel()
        verify {
            queries.insertUser(
                AuthorizedEntity(
                    qrCode = "USR-99",
                    name = "Manuel Lucas",
                    role = "ADMIN",
                    lastUpdated = 1769079870924L
                )
            )
        }
    }
}
