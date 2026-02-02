package com.example.domain.repository

import com.example.domain.model.DeviceConfig
import com.example.core_common.dispatchers.result.Result

interface DeviceRepository {
    fun getDeviceId(): String
    fun getDeviceName(): String
    fun isDeviceSecure(): Boolean
    suspend fun registerDevice(serialNumber: String): Result<DeviceConfig>
}
