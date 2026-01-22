package com.example.domain.usecases

import com.example.domain.model.DeviceConfig
import com.example.domain.repository.DeviceRepository

class InitializeDeviceUseCase(private val repository: DeviceRepository) {
    suspend operator fun invoke(serialNumber: String): Result<DeviceConfig> {
        return if (repository.isDeviceSecure()) {
            repository.registerDevice(serialNumber)
        } else {
            Result.failure(Exception("Security violation: Device is not secure"))
        }
    }
}
