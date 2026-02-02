package com.example.data.repository

import com.example.core_common.dispatchers.DispatcherProvider
import com.example.data.datasources.RecogidasLocalDataSource
import com.example.data.datasources.RecogidasRemoteDataSource
import com.example.data.mapper.RecogidaMapper
import com.example.domain.model.ScanResult
import com.example.domain.repository.RecogidasRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class RecogidasRepositoryImpl(
    private val remoteDataSource: RecogidasRemoteDataSource,
    private val localDataSource: RecogidasLocalDataSource,
    private val mapper: RecogidaMapper,
    private val dispatchers: DispatcherProvider
) : RecogidasRepository {

    override fun validateQr(qr: String): Flow<ScanResult> = flow {
        emit(ScanResult.Loading)

        try {
            // 1. Check local cache for immediate feedback if necessary
            val cachedPerson = localDataSource.getPersonnelByQr(qr)

            if (cachedPerson != null) {
                emit(mapper.mapToDomain(cachedPerson))
            } else {
                // 2. Fallback or verify with Remote
                val remoteResponse = remoteDataSource.validateQr(qr)
                val domainResult = mapper.mapToDomain(remoteResponse)

                // 3. Update local cache
                localDataSource.savePersonnel(mapper.mapToEntity(domainResult, qr))
                emit(domainResult)
            }
        } catch (e: Exception) {
            emit(ScanResult.Error(e.message ?: "Unknown Error"))
        }
    }.flowOn(dispatchers.io)

    override suspend fun syncAuthorizedPersonnel() {
        withContext(dispatchers.io) {
            try {
                val remoteList = remoteDataSource.getAuthorizedPersonnel()
                val entities = remoteList.map { mapper.mapToEntity(it) }

                localDataSource.clearAndSaveAll(entities)
            } catch (e: Exception) {
                // Log error or throw domain-specific exception
                throw e
            }
        }
    }
}
