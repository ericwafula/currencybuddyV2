package tech.ericwathome.core.local.source.converter

import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import tech.ericwathome.core.local.preference.LocalPreferenceSource

internal class DefaultConverterPreferences(
    private val localPreferenceSource: LocalPreferenceSource,
) : ConverterPreferences {
    object Keys {
        val LAST_METADATA_SYNC_TIMESTAMP = longPreferencesKey("last_metadata_sync_timestamp")
        val LAST_EXCHANGE_RATE_SYNC_TIMESTAMP = longPreferencesKey("last_exchange_rate_sync_timestamp")
    }

    override val lastMetadataSyncTimestamp: Flow<Long?>
        get() = localPreferenceSource.getNullable(Keys.LAST_METADATA_SYNC_TIMESTAMP)

    override val lastExchangeRateSyncTimestamp: Flow<Long?>
        get() = localPreferenceSource.getNullable(Keys.LAST_EXCHANGE_RATE_SYNC_TIMESTAMP)

    override suspend fun setLastMetadataSyncTimestamp(value: Long) {
        localPreferenceSource.saveOrUpdate(Keys.LAST_METADATA_SYNC_TIMESTAMP, value)
    }

    override suspend fun setLastExchangeRateSyncTimestamp(value: Long) {
        localPreferenceSource.saveOrUpdate(Keys.LAST_EXCHANGE_RATE_SYNC_TIMESTAMP, value)
    }
}