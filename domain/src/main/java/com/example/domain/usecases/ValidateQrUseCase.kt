package com.example.domain.usecases

import com.example.core_common.usecase.FlowUseCase
import com.example.domain.model.ScanResult
import com.example.domain.repository.RecogidasRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class ValidateQrUseCase(
    private val repository: RecogidasRepository
): FlowUseCase<String, ScanResult>() {
    private val inditexRegex = Regex("^INDITEX_[A-Z0-9]{6}$")
    override fun run(params: String): Flow<ScanResult> = flow {
        emit(ScanResult.Loading)
        val sanitizedQr = params.trim().uppercase()

        if (!sanitizedQr.matches(inditexRegex)) {
            emit(ScanResult.FormatError(read = sanitizedQr))
            return@flow
        }

        emitAll(
            repository.validateQr(sanitizedQr)
                .catch { emit(ScanResult.Error(it.message ?: "Validation failed")) }
        )
    }
}
