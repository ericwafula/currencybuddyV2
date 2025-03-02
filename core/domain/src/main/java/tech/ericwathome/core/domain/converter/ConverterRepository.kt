package tech.ericwathome.core.domain.converter

import kotlinx.coroutines.flow.Flow
import tech.ericwathome.core.domain.converter.model.CurrencyMetadata
import tech.ericwathome.core.domain.converter.model.ExchangeRate
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.EmptyResult

interface ConverterRepository {
    val defaultExchangeRate: Flow<ExchangeRate>
    val savedExchangeRates: Flow<List<ExchangeRate>>
    val currencyMetadata: Flow<List<CurrencyMetadata>>

    suspend fun fetchExchangeRate(
        fromCurrencyCode: String,
        toCurrencyCode: String,
        amount: Double,
        isDefault: Boolean,
    ): EmptyResult<DataError>

    suspend fun syncCurrencyMetadata(): EmptyResult<DataError>

    suspend fun deleteLocalExchangeRate(exchangeRate: ExchangeRate)

    suspend fun clearLocalExchangeRates()

    suspend fun clearLocalCurrencyMetadata()

    suspend fun syncSavedExchangeRates(): EmptyResult<DataError>
}