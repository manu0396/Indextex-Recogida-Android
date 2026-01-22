package com.example.data_core.hardware

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Vibrator
import android.os.VibrationEffect
import android.util.Log
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class PdaHardwareWrapperImpl(
    private val context: Context
) : PdaHardwareWrapper {

    private val TAG = "PdaHardwareWrapperImpl"
    private val _scanResults = MutableSharedFlow<String>(replay = 0)
    override val scanResults: SharedFlow<String> = _scanResults.asSharedFlow()

    private val scannerReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val scanData = intent?.getStringExtra("com.inditex.scan.DATA")
                ?: intent?.getStringExtra("data")

            scanData?.let {
                _scanResults.tryEmit(it)
            }
        }
    }

    init {
        val filter = IntentFilter().apply {
            addAction("com.inditex.recogida.SCAN_ACTION")
            addCategory(Intent.CATEGORY_DEFAULT)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(scannerReceiver, filter, Context.RECEIVER_EXPORTED)
        } else {
            context.registerReceiver(scannerReceiver, filter)
        }
    }

    override fun toggleLaser(enabled: Boolean) {
        val intent = Intent("com.inditex.hardware.LASER_CONTROL").apply {
            putExtra("enabled", enabled)
        }
        context.sendBroadcast(intent)
    }

    @Suppress("DEPRECATION")
    override fun triggerVibration(duration: Long) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(duration)
            }
        }
    }

    override fun release() {
        try {
            context.unregisterReceiver(scannerReceiver)
        } catch (e: Exception) {
            Log.e(TAG, "Error unregistering receiver: $e")
        }
    }
}
