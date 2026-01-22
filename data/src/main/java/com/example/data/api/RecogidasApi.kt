package com.example.data.api

import com.example.data.models.DeviceConfigDto
import com.example.data.models.IdentificationRequest
import com.example.data.models.IdentificationResponse
import com.example.data.models.RecogidaResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RecogidasApi {
    @GET("device/config")
    suspend fun getDeviceConfig(): DeviceConfigDto

    @POST("device/identify")
    suspend fun identifyDevice(@Body request: IdentificationRequest): IdentificationResponse

    @GET("recogidas/validate/{qr}")
    suspend fun validateQr(@Path("qr") qr: String): RecogidaResponse

    @GET("recogidas/all")
    suspend fun getAuthorizedPersonnel(): List<RecogidaResponse>
}
