package com.example.domain.usecases

import com.example.core_common.dispatchers.DispatcherProvider
import com.example.core_common.dispatchers.result.Result
import com.example.core_common.exception.AppExceptions
import com.example.core_common.usecase.UseCase
import com.example.domain.repository.RecogidasRepository
import kotlinx.coroutines.withContext


class SyncAuthorizedUserUseCase(
    private val repository: RecogidasRepository,
    private val dispatchers: DispatcherProvider
) : UseCase<Unit, Result<Unit>>() {

    override suspend fun run(params: Unit): Result<Unit> = withContext(dispatchers.io) {
        try {
            repository.syncAuthorizedPersonnel()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(AppExceptions.from(e))
        }
    }
}
