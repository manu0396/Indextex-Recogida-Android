package com.example.data.repository

import android.content.Context
import android.os.Build
import androidx.core.content.edit
import com.example.core_common.dispatchers.result.Result
import com.example.core_common.exception.AppExceptions
import com.example.domain.model.DeviceConfig
import com.example.domain.repository.DeviceRepository
import java.util.UUID

class DeviceRepositoryImpl(
    private val context: Context,
) : DeviceRepository {

    private val prefs by lazy {
        context.getSharedPreferences("app_secure_prefs", Context.MODE_PRIVATE)
    }

    override fun getDeviceId(): String {
        val key = "secure_installation_id"
        return prefs.getString(key, null) ?: synchronized(this) {
            prefs.getString(key, null) ?: run {
                val newId = UUID.randomUUID().toString()
                prefs.edit { putString(key, newId) }
                newId
            }
        }
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
            Result.Success(config)
        } catch (e: Exception) {
            Result.Error(AppExceptions.from(e))
        }
    }
}
