package com.example.data_core.hardware

import kotlinx.coroutines.flow.SharedFlow

interface PdaHardwareWrapper {
    val scanResults: SharedFlow<String>
    fun toggleLaser(enabled: Boolean)
    fun triggerVibration(duration: Long = 200)
    fun release()
}
