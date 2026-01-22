package com.example.data_core.monitoring

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics

class MonitoringManagerImpl(
    private val analytics: FirebaseAnalytics,
    private val crashlytics: FirebaseCrashlytics
) : MonitoringManager {

    override fun logEvent(name: String, params: Map<String, Any>?) {
        val bundle = params?.let { map ->
            Bundle().apply {
                map.forEach { (key, value) ->
                    when (value) {
                        is String -> putString(key, value)
                        is Int -> putInt(key, value)
                        is Long -> putLong(key, value)
                        is Double -> putDouble(key, value)
                        is Boolean -> putBoolean(key, value)
                        else -> putString(key, value.toString())
                    }
                }
            }
        }
        analytics.logEvent(name, bundle)
    }

    override fun logError(throwable: Throwable, message: String?) {
        message?.let {
            crashlytics.log("Error Message: $it")
        }
        crashlytics.recordException(throwable)
    }

    override fun setUserId(userId: String) {
        analytics.setUserId(userId)
        crashlytics.setUserId(userId)
    }

    override fun setUserProperty(key: String, value: String) {
        analytics.setUserProperty(key, value)
        crashlytics.setCustomKey(key, value)
    }

    override fun addBreadcrumb(message: String) {
        // Crashlytics logs act as breadcrumbs in the crash report
        crashlytics.log(message)
    }
}
