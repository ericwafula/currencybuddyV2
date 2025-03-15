package tech.ericwathome.core.domain.converter

import kotlinx.coroutines.flow.Flow
import tech.ericwathome.core.domain.converter.model.CurrencyMetadata
import tech.ericwathome.core.domain.converter.model.ExchangeRate
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.EmptyResult

interface ConverterRepository {
    val savedExchangeRatesObservable: Flow<List<ExchangeRate>>
    val currencyMetadataObservable: Flow<List<CurrencyMetadata>>
    val lastMetadataSyncTimestampObservable: Flow<Long?>
    val lastExchangeRateSyncTimestampObservable: Flow<Long?>
    val isMetadataSyncingObservable: Flow<Boolean?>
    val isExchangeRateSyncingObservable: Flow<Boolean?>
    val exchangeRateObservable: Flow<ExchangeRate?>

    suspend fun fetchExchangeRate(
        fromCurrencyCode: String,
        toCurrencyCode: String,
        baseFlag: String? = null,
        quoteFlag: String? = null,
        amount: Double,
    ): EmptyResult<DataError>

    suspend fun syncCurrencyMetadata(): EmptyResult<DataError>

    fun observeFilteredCurrencyMetaData(query: String): Flow<List<CurrencyMetadata>>

    suspend fun deleteExchangeRate()

    suspend fun clearLocalExchangeRates()

    suspend fun clearLocalCurrencyMetadata()

    suspend fun syncSavedExchangeRates(): EmptyResult<DataError>

    suspend fun setExchangeRate(value: ExchangeRate): EmptyResult<DataError.Local>

    fun getSavedExchangeRate(): ExchangeRate?
}