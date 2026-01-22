package com.example.data_core.hardware

interface PdaHardwareManager {
    fun getSerialNumber(): String
    fun toggleFrontCamera(enable: String)
}
