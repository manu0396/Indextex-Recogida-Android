package com.example.data.datasources

import com.example.data.api.RecogidasApi
import com.example.data.models.RecogidaResponse

interface RecogidasRemoteDataSource {
    suspend fun validateQr(qr: String): RecogidaResponse
    suspend fun getAuthorizedPersonnel(): List<RecogidaResponse>
}

class RecogidasRemoteDataSourceImpl(
    private val api: RecogidasApi,
) : RecogidasRemoteDataSource {
    override suspend fun validateQr(qr: String): RecogidaResponse {
        return api.validateQr(qr)
    }

    override suspend fun getAuthorizedPersonnel(): List<RecogidaResponse> {
        return api.getAuthorizedPersonnel()
    }
}
