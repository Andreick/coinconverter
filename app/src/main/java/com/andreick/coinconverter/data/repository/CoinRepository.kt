package com.andreick.coinconverter.data.repository

import com.andreick.coinconverter.data.model.ExchangeResponseValue
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    suspend fun getExchangeValue(coins: String): Flow<ExchangeResponseValue>

    suspend fun save(exchange: ExchangeResponseValue)

    fun list(): Flow<List<ExchangeResponseValue>>
}