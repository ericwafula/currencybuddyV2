package tech.ericwathome.core.domain.converter

import kotlinx.coroutines.flow.Flow
import tech.ericwathome.core.domain.converter.model.CurrencyMetadata
import tech.ericwathome.core.domain.converter.model.ExchangeRate
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.EmptyResult

interface LocalConverterDataSource {
    val savedExchangeRatesObservable: Flow<List<ExchangeRate>>
    val lastMetadataSyncTimestamp: Flow<Long?>
    val lastExchangeRateSyncTimestamp: Flow<Long?>
    val isMetadataSyncingObservable: Flow<Boolean?>
    val isExchangeRateSyncingObservable: Flow<Boolean?>
    val exchangeRateObservable: Flow<ExchangeRate?>
    val hasNotificationPermission: Flow<Boolean?>

    suspend fun setLastMetadataSyncTimestamp(value: Long)

    suspend fun setLastExchangeRateSyncTimestamp(value: Long)

    suspend fun setIsMetadataSyncing(value: Boolean)

    suspend fun setIsSelectedCurrencyPairSyncing(value: Boolean)

    suspend fun retrieveSavedExchangeRates(): List<ExchangeRate>

    suspend fun upsertLocalExchangeRate(exchangeRate: ExchangeRate): EmptyResult<DataError.Local>

    suspend fun deleteLocalExchangeRate(exchangeRate: ExchangeRate)

    suspend fun clearLocalExchangeRates()

    fun observeCurrencyMetadata(): Flow<List<CurrencyMetadata>>

    fun observeFilteredCurrencyMetaData(query: String): Flow<List<CurrencyMetadata>>

    suspend fun upsertLocalCurrencyMetaData(
        currencyMetaData: CurrencyMetadata,
        rate: Double,
    ): EmptyResult<DataError.Local>

    suspend fun upsertLocalCurrencyMetaDataList(currencyMetadataList: List<CurrencyMetadata>): EmptyResult<DataError.Local>

    suspend fun clearLocalCurrencyMetadata()

    suspend fun setExchangeRate(value: ExchangeRate): EmptyResult<DataError.Local>

    suspend fun setHasNotificationPermission(value: Boolean)

    suspend fun deleteExchangeRate()
}