package com.example.data.models

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object for Device Configuration
 */
data class DeviceConfigDto(
    @SerializedName("config_id") val configId: String,
    @SerializedName("environment") val environment: String,
    @SerializedName("features_enabled") val features: List<String>
)
