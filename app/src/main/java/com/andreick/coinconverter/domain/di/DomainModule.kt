package com.andreick.coinconverter.domain.di

import com.andreick.coinconverter.domain.GetExchangeValueUseCase
import com.andreick.coinconverter.domain.ListExchangeUseCase
import com.andreick.coinconverter.domain.SaveExchangeUseCase
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object DomainModule {

    fun load() {
        loadKoinModules(useCaseModules())
    }

    private fun useCaseModules(): Module {
        return module {
            factory { GetExchangeValueUseCase(get()) }
            factory { ListExchangeUseCase(get()) }
            factory { SaveExchangeUseCase(get()) }
        }
    }
}