package com.example.data_core.di

import com.example.data_core.api.ProsegurAdapterApi
import com.example.data_core.hardware.PdaHardwareWrapper
import com.example.data_core.hardware.PdaHardwareWrapperImpl
import com.example.data_core.monitoring.MonitoringManager
import com.example.data_core.monitoring.MonitoringManagerImpl
import com.example.data_core.network.ProsegurInterceptor
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.firestore
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val dataCoreModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }
    // Hardware HAL
    single<PdaHardwareWrapper> { PdaHardwareWrapperImpl(get()) }
    single { Firebase.firestore }
    single { Firebase.auth }
    single {
        OkHttpClient.Builder()
            .addInterceptor(ProsegurInterceptor(get()))
            .build()
    }
    // Networking
    single {
        Retrofit.Builder()
            .baseUrl("https://prosegur-adapter.inditex.com/")
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .client(get())
            .build()
            .create(ProsegurAdapterApi::class.java)
    }

    single { FirebaseCrashlytics.getInstance() }

    single<MonitoringManager> { MonitoringManagerImpl(crashlytics = get(), analytics = get()) }
}
