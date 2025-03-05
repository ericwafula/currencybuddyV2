package tech.ericwathome.core.local.source.converter

import kotlinx.coroutines.flow.Flow

interface ConverterPreferences {
    val lastMetadataSyncTimestamp: Flow<Long?>
    val lastExchangeRateSyncTimestamp: Flow<Long?>
    val isMetadataSyncing: Flow<Boolean?>
    val isExchangeRateSyncing: Flow<Boolean?>

    suspend fun setLastMetadataSyncTimestamp(value: Long)

    suspend fun setLastExchangeRateSyncTimestamp(value: Long)

    suspend fun setIsMetadataSyncing(value: Boolean)

    suspend fun setIsExchangeRateSyncing(value: Boolean)
}