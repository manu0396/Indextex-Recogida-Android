package com.example.data.di

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.data.api.RecogidasApi
import com.example.data.datasources.RecogidasLocalDataSource
import com.example.data.datasources.RecogidasLocalDataSourceImpl
import com.example.data.datasources.RecogidasRemoteDataSource
import com.example.data.datasources.RecogidasRemoteDataSourceImpl
import com.example.data.db.RecogidaDatabase
import com.example.data.mapper.RecogidaMapper
import com.example.data.repository.DeviceRepositoryImpl
import com.example.data.repository.MockRecogidasRepository
import com.example.data.repository.RecogidasRepositoryImpl
import com.example.domain.repository.DeviceRepository
import com.example.domain.repository.RecogidasRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    single<RecogidaDatabase> {
        RecogidaDatabase(
            driver = AndroidSqliteDriver(RecogidaDatabase.Schema, get(), "recogida.db")
        )
    }

    single<RecogidasLocalDataSource> {
        RecogidasLocalDataSourceImpl(database = get(), dispatchers = get())
    }

    single<RecogidasRemoteDataSource> {
        RecogidasRemoteDataSourceImpl(api = get())
    }

    single<DeviceRepository> { DeviceRepositoryImpl(androidContext()) }

    single<RecogidasRepository> {
        RecogidasRepositoryImpl(get(), get(), get(), get())
    }

    single<RecogidaMapper> { RecogidaMapper() }

    // --- FIX: FORCE MOCK TO BYPASS 404 ---
    // TODO: Revert to 'if/else' logic when Server Prod is fixed
    single<RecogidasRepository> {
        val isDebug = getProperty<Boolean>("IS_DEBUG_MODE", false)
        if (isDebug) {
            MockRecogidasRepository()
        } else {
            RecogidasRepositoryImpl(get(), get(), get(), get())
        }
    }

    /* // OLD LOGIC (Disabled)
    if (BuildConfig.FLAVOR == "spainPre") {
        single<RecogidasRepository> { MockRecogidasRepository() }
    } else {
        single<RecogidasRepository> { RecogidaRepositoryImpl(get(), get(), get(), get()) }
    }
    */
    // -------------------------------------

    single {
        Retrofit.Builder()
            .baseUrl("https://api.inditex.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<RecogidasApi> {
        get<Retrofit>().create(RecogidasApi::class.java)
    }
}
