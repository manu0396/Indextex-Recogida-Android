package com.example.data_core.monitoring

interface MonitoringManager {
    fun logEvent(name: String, params: Map<String, Any>? = null)
    fun logError(throwable: Throwable, message: String? = null)
    fun setUserId(userId: String)
    fun setUserProperty(key: String, value: String)
    fun addBreadcrumb(message: String)
}
