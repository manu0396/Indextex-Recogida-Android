package com.example.pda_recogida_android

import android.app.Application
import com.example.core_common.di.coreCommonModule
import com.example.data.di.networkModule
import com.example.data_core.di.dataCoreModule
import com.example.recogidas_presentation.di.recogidasModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import com.example.domain.di.domainModule
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(listOf(
                coreCommonModule,
                networkModule,
                dataCoreModule,
                domainModule,
                recogidasModule
            )
            )
        }
    }
}
