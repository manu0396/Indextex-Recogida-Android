package com.example.data.models

import com.google.gson.annotations.SerializedName

/**
 * Response for device identification
 */
data class IdentificationResponse(
    @SerializedName("auth_token") val token: String,
    @SerializedName("valid_until") val expirationTimestamp: Long
)
