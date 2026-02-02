package com.example.pda_recogida_android

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.example.core_common.di.coreCommonModule
import com.example.data.di.networkModule
import com.example.data_core.di.dataCoreModule
import com.example.domain.di.domainModule
import com.example.recogidas_presentation.di.presentationModule
import com.example.recogidas_presentation.ui.analyzer.QrCodeAnalyzer
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.inject.Provider
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.verify.definition
import org.koin.test.verify.injectedParameters
import org.koin.test.verify.verify
import java.util.concurrent.Executor
import java.util.concurrent.ScheduledExecutorService

class KoinModulesTest : KoinTest {

    @Test
    fun checkAllModules() {
        val allModules = module {
            includes(
                networkModule,
                dataCoreModule,
                coreCommonModule,
                domainModule,
                presentationModule
            )
        }

        allModules.verify(
            extraTypes = listOf(
                Context::class,
                SavedStateHandle::class,
                FirebaseApp::class,
                FirebaseAuth::class,
                FirebaseFirestore::class,
                Provider::class,
                Executor::class,
                ScheduledExecutorService::class
            ),
            injections = injectedParameters(
                definition<QrCodeAnalyzer>(Function1::class)
            )
        )
    }
}
