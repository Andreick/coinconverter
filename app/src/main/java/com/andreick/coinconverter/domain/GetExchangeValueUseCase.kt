package com.andreick.coinconverter.domain

import com.andreick.coinconverter.core.UseCase
import com.andreick.coinconverter.data.model.ExchangeResponseValue
import com.andreick.coinconverter.data.repository.CoinRepository
import kotlinx.coroutines.flow.Flow

class GetExchangeValueUseCase(
    private val repository: CoinRepository
) : UseCase<String, ExchangeResponseValue>() {

    override suspend fun execute(param: String): Flow<ExchangeResponseValue> {
        return repository.getExchangeValue(param)
    }
}