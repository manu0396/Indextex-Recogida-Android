package com.example.data.repository

import android.util.Log
import com.example.domain.model.ScanResult
import com.example.domain.repository.RecogidasRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class MockRecogidasRepository : RecogidasRepository {

    override fun validateQr(qr: String): Flow<ScanResult> = flow {
        delay(1000)
        val id = qr.substringAfter("-")
        when (id) {
            "000000" -> {
                // Simulate a valid format but unauthorized user in the DB
                emit(ScanResult.Error("Usuario no autorizado para esta ruta (ID: $id)"))
            }
            "999999" -> {
                // Simulate a server timeout/crash
                throw Exception("Error de conexión con el servidor de Inditex")
            }
            else -> {
                // Default success case
                emit(ScanResult.Success(
                    name = "Manuel Lucas",
                    type = "Sénior - Código $id"
                ))
            }
        }
    }.catch { e ->
        emit(ScanResult.Error(e.message ?: "Error desconocido"))
    }

    override suspend fun syncAuthorizedPersonnel() {
        delay(500)
    }
}
