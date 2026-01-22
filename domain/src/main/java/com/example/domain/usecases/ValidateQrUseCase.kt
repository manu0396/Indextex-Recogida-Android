package com.example.domain.usecases

import com.example.domain.model.ScanResult
import com.example.domain.repository.RecogidasRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart

class ValidateQrUseCase(
    private val repository: RecogidasRepository
) {
    /**
     * Executes the validation logic.
     * * @param qr The raw string data from the PDA or Camera scanner.
     * @return A Flow emitting the state of the scan operation.
     */
    operator fun invoke(qr: String): Flow<ScanResult> {
        return repository.validateQr(qr)
            .onStart {
                // Ensure UI knows we are processing even before repository emits
                emit(ScanResult.Loading)
            }
            .catch { throwable ->
                // Basic domain-level error mapping
                emit(ScanResult.Error(throwable.message ?: "Validation failed"))
            }
    }
}
