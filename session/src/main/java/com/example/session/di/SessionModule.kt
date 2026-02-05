package com.example.session.di

import com.example.domain.repository.SessionRepository
import com.example.session.datasource.SessionLocalDataSource
import com.example.session.repository.SessionRepositoryImpl
import com.example.domain.usecases.GetSessionInfoUseCase
import com.example.domain.usecases.LogoutUseCase
import com.example.session.datasource.SessionRemoteDataSource
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val sessionModule = module {
    singleOf(::SessionLocalDataSource)
    singleOf(::SessionRemoteDataSource)

    single<SessionRepository> {
        SessionRepositoryImpl(
            localDataSource = get(),
            remoteDataSource = get(),
            appVersion = "1.0.0",
            appFlavor = "PRO"
        )
    }

    factoryOf(::GetSessionInfoUseCase)
    factoryOf(::LogoutUseCase)
}
