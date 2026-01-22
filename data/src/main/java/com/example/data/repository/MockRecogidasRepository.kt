package com.example.data.repository

import com.example.data.models.RecogidaResponse
import com.example.domain.model.ScanResult
import com.example.domain.repository.RecogidasRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockRecogidasRepository : RecogidasRepository {

    override fun validateQr(qr: String): Flow<ScanResult> = flow {
        emit(ScanResult.Loading)
        delay(1500)
        if (qr.startsWith("INDITEX_VALID")) {
            // Mocking the DTO you provided
            val mockDto = RecogidaResponse(
                id = "REC-999",
                fullName = "Manuel Lucas",
                roleType = "Senior Developer",
                timestamp = System.currentTimeMillis()
            )
            emit(
                ScanResult.Success(
                name = mockDto.fullName ?: "Unknown",
                type = mockDto.roleType ?: "General"
            ))
        } else {
            emit(ScanResult.Error("Unauthorized or invalid QR code format."))
        }
    }

    override suspend fun syncAuthorizedPersonnel() {
        delay(500)
    }
}
