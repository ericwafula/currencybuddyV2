package tech.ericwathome.core.local.source.converter

import kotlinx.coroutines.flow.Flow
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.EmptyResult
import tech.ericwathome.core.local.model.datastore.ExchangeRatePreferences

interface ConverterPreferences {
    val lastMetadataSyncTimestamp: Flow<Long?>
    val lastExchangeRateSyncTimestamp: Flow<Long?>
    val isMetadataSyncing: Flow<Boolean?>
    val isExchangeRateSyncing: Flow<Boolean?>
    val exchangeRate: Flow<ExchangeRatePreferences?>

    suspend fun setLastMetadataSyncTimestamp(value: Long)

    suspend fun setLastExchangeRateSyncTimestamp(value: Long)

    suspend fun setIsMetadataSyncing(value: Boolean)

    suspend fun setIsExchangeRateSyncing(value: Boolean)

    suspend fun setExchangeRate(value: ExchangeRatePreferences): EmptyResult<DataError.Local>

    suspend fun deleteExchangeRate()
}