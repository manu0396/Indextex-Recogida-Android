package com.example.domain.usecases

import com.example.core_common.exception.AppExceptions
import com.example.core_common.usecase.UseCase
import com.example.domain.model.DeviceConfig
import com.example.domain.repository.DeviceRepository
import com.example.core_common.dispatchers.result.Result

class InitializeDeviceUseCase(
    private val repository: DeviceRepository
) : UseCase<String, Result<DeviceConfig>>() {

    override suspend fun run(params: String): Result<DeviceConfig> {
        return if (repository.isDeviceSecure()) {
            repository.registerDevice(params)
        } else {
            Result.Error(AppExceptions.SecurityViolation("Device is not secure"))
        }
    }
}
