package com.example.domain.usecases

import com.example.core_common.dispatchers.DispatcherProvider
import com.example.domain.repository.RecogidasRepository
import kotlinx.coroutines.withContext

class SyncAuthorizedUserUseCase(
    private val repository: RecogidasRepository,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(): Result<Unit> = withContext(dispatchers.io) {
        return@withContext try {
            repository.syncAuthorizedPersonnel()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
