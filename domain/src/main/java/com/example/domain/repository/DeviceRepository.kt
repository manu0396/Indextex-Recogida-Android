package com.example.domain.repository

import com.example.domain.model.DeviceConfig

interface DeviceRepository {
    fun getDeviceId(): String
    fun getDeviceName(): String
    fun isDeviceSecure(): Boolean
    suspend fun registerDevice(serialNumber: String): Result<DeviceConfig>
}
