package com.andreick.coinconverter

import android.app.Application
import com.andreick.coinconverter.data.di.DataModule
import com.andreick.coinconverter.domain.di.DomainModule
import com.andreick.coinconverter.presentation.di.PresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin { androidContext(this@App) }
        DataModule.load()
        DomainModule.load()
        PresentationModule.load()
    }
}