package com.example.pda_recogida_android

import android.app.Application
import com.example.core_common.di.coreCommonModule
import com.example.data.di.networkModule
import com.example.data_core.di.dataCoreModule
import com.example.domain.di.domainModule
import com.example.feature_settings.di.settingsModule
import com.example.recogidas_presentation.di.presentationModule
import com.example.session.di.sessionModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            properties(mapOf("IS_DEBUG_MODE" to BuildConfig.DEBUG))
            modules(
                listOf(
                    coreCommonModule,
                    networkModule,
                    dataCoreModule,
                    domainModule,
                    presentationModule,
                    settingsModule,
                    sessionModule
                )
            )
        }
    }
}
