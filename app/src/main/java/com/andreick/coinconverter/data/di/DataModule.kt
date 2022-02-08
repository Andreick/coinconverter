package com.andreick.coinconverter.data.di

import android.util.Log
import com.andreick.coinconverter.data.database.AppDatabase
import com.andreick.coinconverter.data.repository.CoinRepository
import com.andreick.coinconverter.data.repository.CoinRepositoryImplementer
import com.andreick.coinconverter.data.services.AwesomeService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object DataModule {

    private const val HTTP_TAG = "OkHttp"

    fun load() {
        loadKoinModules(networkModule() + repositoryModule() + databaseModule())
    }

    private fun networkModule(): Module {
        return module {
            single {
                val interceptor = HttpLoggingInterceptor { Log.d(HTTP_TAG, ": $it") }.apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                OkHttpClient.Builder().addInterceptor(interceptor).build()
            }
            single { GsonConverterFactory.create(GsonBuilder().create()) }
            single { createService<AwesomeService>(get(), get()) }
        }
    }

    private inline fun <reified T> createService(
        client: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): T {
        return Retrofit.Builder()
            .baseUrl("https://economia.awesomeapi.com.br")
            .client(client)
            .addConverterFactory(converterFactory)
            .build()
            .create()
    }

    private fun repositoryModule(): Module {
        return module {
            single<CoinRepository> { CoinRepositoryImplementer(get(), get()) }
        }
    }

    private fun databaseModule(): Module {
        return module {
            single { AppDatabase.getInstance(androidApplication()) }
        }
    }
}