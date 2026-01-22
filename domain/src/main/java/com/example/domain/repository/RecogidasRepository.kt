package com.example.domain.repository

import com.example.domain.model.ScanResult
import kotlinx.coroutines.flow.Flow

interface RecogidasRepository {
    fun validateQr(qr: String): Flow<ScanResult>
    suspend fun syncAuthorizedPersonnel()
}
