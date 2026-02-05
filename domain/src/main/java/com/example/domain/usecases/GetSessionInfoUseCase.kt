package com.example.domain.usecases

import com.example.core_common.dispatchers.DispatcherProvider
import com.example.core_common.dispatchers.result.Result
import com.example.core_common.exception.AppExceptions
import com.example.core_common.usecase.UseCase
import com.example.domain.model.SessionInfo
import com.example.domain.repository.SessionRepository
import kotlinx.coroutines.withContext

class GetSessionInfoUseCase(
    private val repository: SessionRepository,
    private val dispatchers: DispatcherProvider
) : UseCase<Unit, Result<SessionInfo>>() { // 🟢 Returns SessionInfo now

    override suspend fun run(params: Unit): Result<SessionInfo> = withContext(dispatchers.io) {
        try {
            val info = repository.getSessionInfo()
            Result.Success(info)
        } catch (e: Exception) {
            Result.Error(AppExceptions.from(e))
        }
    }
}
