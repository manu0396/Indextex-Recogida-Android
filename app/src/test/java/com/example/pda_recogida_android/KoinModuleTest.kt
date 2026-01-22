package com.example.data_core

import com.example.data_core.di.networkModule
import com.example.feature_recogidas_android.di.recogidasModule
import org.junit.Test
import org.koin.core.context.koinApplication
import org.koin.test.KoinTest

class KoinModulesTest : KoinTest {

    @Test
    fun checkAllModules() {
        koinApplication {
            modules(
                networkModule,
                networkModule,
                recogidasModule
            )
        }.checkModules()
    }
}
