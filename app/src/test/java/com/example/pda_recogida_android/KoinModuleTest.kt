package com.example.pda_recogida_android

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.example.core_common.di.coreCommonModule
import com.example.data.di.networkModule
import com.example.data_core.di.dataCoreModule
import com.example.domain.di.domainModule
import com.example.recogidas_presentation.di.presentationModule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.verify.verify

class KoinModulesTest : KoinTest {
    @Test
    fun checkAllModules() {
        val allModules = module {
            includes(
                networkModule, dataCoreModule, coreCommonModule,
                domainModule, presentationModule
            )
        }
        allModules.verify(
            extraTypes = listOf(
                Context::class,
                SavedStateHandle::class
            )
        )
    }
}
