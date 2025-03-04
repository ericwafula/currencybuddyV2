package tech.ericwathome.core.local.source.converter

import kotlinx.coroutines.flow.Flow

interface ConverterPreferences {
    val lastMetadataSyncTimestamp: Flow<Long?>
    val lastExchangeRateSyncTimestamp: Flow<Long?>

    suspend fun setLastMetadataSyncTimestamp(value: Long)
    suspend fun setLastExchangeRateSyncTimestamp(value: Long)
}