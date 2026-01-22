package com.example.data.models

import com.google.gson.annotations.SerializedName

/**
 * Request body for device identification
 */
data class IdentificationRequest(
    @SerializedName("serial_number") val serialNumber: String,
    @SerializedName("model") val model: String
)
