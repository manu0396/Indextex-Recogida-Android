package com.example.data_core.models

import kotlinx.serialization.Serializable

@Serializable
data class DeviceConfigDto(val mode: String, val centerId: String)
