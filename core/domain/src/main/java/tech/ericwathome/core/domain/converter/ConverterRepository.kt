package tech.ericwathome.core.domain.converter

import kotlinx.coroutines.flow.Flow
import tech.ericwathome.core.domain.converter.model.CurrencyMetaData
import tech.ericwathome.core.domain.converter.model.ExchangeRate
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.EmptyResult

interface ConverterRepository {
    suspend fun fetchExchangeRate(
        fromCurrencyCode: String,
        toCurrencyCode: String,
        amount: Double,
        isSelected: Boolean,
    ): EmptyResult<DataError>

    fun observeSelectedExchangeRate(): Flow<ExchangeRate>

    fun observeNonSelectedExchangeRates(): Flow<List<ExchangeRate>>

    fun observeCurrencyMetaData(): Flow<List<CurrencyMetaData>>

    suspend fun fetchCurrencyMetaData(): EmptyResult<DataError>

    suspend fun deleteLocalExchangeRate(exchangeRate: ExchangeRate)

    suspend fun clearLocalExchangeRates()

    suspend fun clearLocalCurrencyMetaData()

    suspend fun syncSavedExchangeRates()
}