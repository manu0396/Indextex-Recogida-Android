package com.example.domain.di

import com.example.domain.usecases.InitializeDeviceUseCase
import com.example.domain.usecases.ValidateQrUseCase
import com.example.domain.usecases.SyncAuthorizedUserUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { InitializeDeviceUseCase(repository = get()) }
    factory { ValidateQrUseCase(repository = get()) }
    factory { SyncAuthorizedUserUseCase(repository = get(), dispatchers = get()) }
}
