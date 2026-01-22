package com.example.data_core.network

import com.example.data_core.hardware.PdaHardwareManager
import okhttp3.Interceptor
import okhttp3.Response

class ProsegurInterceptor(private val hardware: PdaHardwareManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder()
            .header("X-Device-SN", hardware.getSerialNumber())
            .header("Content-Type", "application/json")
            .method(original.method, original.body)
            .build()
        return chain.proceed(request)
    }
}
