package com.example.data.models

import com.google.gson.annotations.SerializedName

data class RecogidaResponse(
    @SerializedName("id") val id: String,
    @SerializedName("full_name") val fullName: String?,
    @SerializedName("role_type") val roleType: String?,
    @SerializedName("authorized_at") val timestamp: Long?
)
