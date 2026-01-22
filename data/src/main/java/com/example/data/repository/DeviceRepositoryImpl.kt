package com.example.data.repository

import android.content.Context
import android.os.Build
import android.provider.Settings
import com.example.domain.model.DeviceConfig
import com.example.domain.repository.DeviceRepository

class DeviceRepositoryImpl(
    private val context: Context
) : DeviceRepository {

    override fun getDeviceId(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    override fun getDeviceName(): String {
        return "${Build.MANUFACTURER} ${Build.MODEL}"
    }

    override fun isDeviceSecure(): Boolean {
        val buildTags = Build.TAGS
        return buildTags != null && !buildTags.contains("test-keys")
    }

    override suspend fun registerDevice(serialNumber: String): Result<DeviceConfig> {
        return try {
            val config = DeviceConfig(
                mode = "NORMAL",
                centerId = "ES_Center_01",
                frontCameraActive = false
            )
            Result.success(config)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
