package com.example.data.repository

import com.example.data.utils.DataConstants.MOCK_DELAY
import com.example.domain.model.ScanResult
import com.example.domain.repository.RecogidasRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class MockRecogidasRepository : RecogidasRepository {

    override fun validateQr(qr: String): Flow<ScanResult> = flow {
        emit(ScanResult.Loading)
        delay(MOCK_DELAY)
        if (qr.contains("QR-CODES") || qr.contains("CODES.IO")) {
            emit(ScanResult.Success(name = "Test User", type = "MOCK URL"))
            return@flow
        }
        if (!qr.contains("_") || qr.startsWith("_")) {
            emit(ScanResult.FormatError(
                read = qr.take(15),
                expected = "INDITEX_XXXXXX"
            ))
            return@flow
        }
        when (val id = qr.substringAfter("_")) {
            "000000" -> emit(ScanResult.Error("Usuario no autorizado (ID: $id)"))
            "999999" -> throw Exception("Error de conexión con el servidor")
            else -> emit(ScanResult.Success(
                name = "Manuel Lucas",
                type = "ID: $id"
            ))
        }
    }.catch { e -> emit(ScanResult.Error(e.message + "Unknown Error")) }

    override suspend fun syncAuthorizedPersonnel() {
        delay(500)
    }
}
