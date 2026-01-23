package com.example.recogidas_presentation.di

import com.example.data.repository.RecogidaRepositoryImpl
import com.example.domain.usecases.SyncAuthorizedUserUseCase
import com.example.domain.usecases.ValidateQrUseCase
import com.example.recogidas_presentation.ui.analyzer.QrCodeAnalyzer
import com.example.recogidas_presentation.viewmodel.RecogidasViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import com.example.domain.repository.RecogidasRepository
import com.example.recogidas_presentation.viewmodel.ScannerViewModel

val presentationModule = module {
    factory { ValidateQrUseCase(repository = get()) }
    factory { SyncAuthorizedUserUseCase(repository = get(), dispatchers = get()) }
    factory { (onQrDetected: (String) -> Unit) ->
        QrCodeAnalyzer(context = get(), onQrDetected = onQrDetected)
    }
    single<RecogidasRepository> {
        RecogidaRepositoryImpl(get(), get(), get(), get())
    }
    viewModel {
        RecogidasViewModel(
            validateQrUseCase = get(),
            syncUseCase = get()
        )
    }
    viewModel {
        ScannerViewModel(
            repository = get()
        )
    }
}
