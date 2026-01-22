package com.example.data.datasources

import com.example.data.db.AuthorizedEntity

interface RecogidasLocalDataSource {
    suspend fun getPersonnelByQr(qr: String): AuthorizedEntity?
    suspend fun savePersonnel(entity: AuthorizedEntity)
    suspend fun clearAndSaveAll(entities: List<AuthorizedEntity>)
}
