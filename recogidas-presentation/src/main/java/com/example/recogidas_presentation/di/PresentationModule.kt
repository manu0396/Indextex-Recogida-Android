package com.example.recogidas_presentation.di

import com.example.recogidas_presentation.ui.analyzer.QrCodeAnalyzer
import com.example.recogidas_presentation.viewmodel.RecogidasViewModel
import com.example.recogidas_presentation.viewmodel.ScannerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    factory { (onQrDetected: (String) -> Unit) ->
        QrCodeAnalyzer(onCodeDetected = onQrDetected)
    }
    viewModel {
        RecogidasViewModel(
            validateQrUseCase = get(),
            syncUseCase = get()
        )
    }
    viewModel {
        ScannerViewModel(
            validateQrUseCase = get()
        )
    }
}
