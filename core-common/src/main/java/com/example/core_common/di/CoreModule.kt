package com.example.core_common.di

import com.example.core_common.dispatchers.DefaultDispatcherProvider
import com.example.core_common.dispatchers.DispatcherProvider
import org.koin.dsl.module

val coreCommonModule = module {
    single<DispatcherProvider> { DefaultDispatcherProvider() }
}
