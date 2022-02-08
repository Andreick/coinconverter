package com.andreick.coinconverter.domain

import com.andreick.coinconverter.core.UseCase
import com.andreick.coinconverter.data.model.ExchangeResponseValue
import com.andreick.coinconverter.data.repository.CoinRepository
import kotlinx.coroutines.flow.Flow

class ListExchangeUseCase(
    private val repository: CoinRepository
) : UseCase.NoParam<List<ExchangeResponseValue>>() {

    override suspend fun execute(): Flow<List<ExchangeResponseValue>> {
        return repository.list()
    }
}