package com.example.domain.di

import com.example.core_common.dispatchers.DispatcherProvider
import com.example.domain.repository.DeviceRepository
import com.example.domain.repository.RecogidasRepository
import com.example.domain.usecases.InitializeDeviceUseCase
import com.example.domain.usecases.ValidateQrUseCase
import com.example.domain.usecases.SyncAuthorizedUserUseCase
import org.koin.dsl.module

val domainModule = module {
    factory<InitializeDeviceUseCase> { InitializeDeviceUseCase(repository = get<DeviceRepository>()) }
    factory<ValidateQrUseCase> { ValidateQrUseCase(repository = get<RecogidasRepository>()) }
    factory<SyncAuthorizedUserUseCase> { SyncAuthorizedUserUseCase(repository = get<RecogidasRepository>(), dispatchers = get<DispatcherProvider>()) }
}
