package tech.ericwathome.core.domain.converter

import kotlinx.coroutines.flow.Flow
import tech.ericwathome.core.domain.converter.model.CurrencyMetadata
import tech.ericwathome.core.domain.converter.model.ExchangeRate
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.EmptyResult

interface ConverterRepository {
    val defaultExchangeRateObservable: Flow<ExchangeRate>
    val savedExchangeRatesObservable: Flow<List<ExchangeRate>>
    val currencyMetadataObservable: Flow<List<CurrencyMetadata>>
    val lastMetadataSyncTimestampObservable: Flow<Long?>
    val lastExchangeRateSyncTimestampObservable: Flow<Long?>
    val isMetadataSyncingObservable: Flow<Boolean?>
    val isExchangeRateSyncingObservable: Flow<Boolean?>

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