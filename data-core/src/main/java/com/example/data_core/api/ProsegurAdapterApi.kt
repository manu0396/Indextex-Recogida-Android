package com.example.data_core.api

import com.example.data_core.models.DeviceConfigDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ProsegurAdapterApi {
    @GET("v1/config")
    suspend fun getDeviceConfig(@Query("sn") sn: String): DeviceConfigDto
}
