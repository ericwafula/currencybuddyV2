package tech.ericwathome.core.local.source.converter

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.EmptyResult
import tech.ericwathome.core.local.model.datastore.ExchangeRatePreferences
import tech.ericwathome.core.local.preference.LocalPreferenceSource

internal class DefaultConverterPreferences(
    private val localPreferenceSource: LocalPreferenceSource,
) : ConverterPreferences {
    private val json =
        Json {
            isLenient = true
            ignoreUnknownKeys = true
            prettyPrint = true
        }

    object Keys {
        val LAST_METADATA_SYNC_TIMESTAMP = longPreferencesKey("last_metadata_sync_timestamp")
        val LAST_EXCHANGE_RATE_SYNC_TIMESTAMP = longPreferencesKey("last_exchange_rate_sync_timestamp")
        val IS_METADATA_SYNCING = booleanPreferencesKey("is_metadata_syncing")
        val IS_EXCHANGE_RATE_SYNCING = booleanPreferencesKey("is_exchange_rate_syncing")
        val EXCHANGE_RATE = stringPreferencesKey("exchange_rate")
    }

    override val lastMetadataSyncTimestamp: Flow<Long?>
        get() = localPreferenceSource.getNullable(Keys.LAST_METADATA_SYNC_TIMESTAMP)

    override val lastExchangeRateSyncTimestamp: Flow<Long?>
        get() = localPreferenceSource.getNullable(Keys.LAST_EXCHANGE_RATE_SYNC_TIMESTAMP)

    override val isMetadataSyncing: Flow<Boolean?>
        get() = localPreferenceSource.getNullable(Keys.IS_METADATA_SYNCING)

    override val isExchangeRateSyncing: Flow<Boolean?>
        get() = localPreferenceSource.getNullable(Keys.IS_EXCHANGE_RATE_SYNCING)

    override val exchangeRate: Flow<ExchangeRatePreferences?>
        get() =
            localPreferenceSource.getNullable(Keys.EXCHANGE_RATE).map { exchangeRateJson ->
                exchangeRateJson?.let { json.decodeFromString<ExchangeRatePreferences>(it) }
            }

    override suspend fun setLastMetadataSyncTimestamp(value: Long) {
        localPreferenceSource.saveOrUpdate(Keys.LAST_METADATA_SYNC_TIMESTAMP, value)
    }

    override suspend fun setLastExchangeRateSyncTimestamp(value: Long) {
        localPreferenceSource.saveOrUpdate(Keys.LAST_EXCHANGE_RATE_SYNC_TIMESTAMP, value)
    }

    override suspend fun setIsMetadataSyncing(value: Boolean) {
        localPreferenceSource.saveOrUpdate(Keys.IS_METADATA_SYNCING, value)
    }

    override suspend fun setIsExchangeRateSyncing(value: Boolean) {
        localPreferenceSource.saveOrUpdate(Keys.IS_EXCHANGE_RATE_SYNCING, value)
    }

    override suspend fun setExchangeRate(value: ExchangeRatePreferences): EmptyResult<DataError.Local> {
        val exchangeRateJson = json.encodeToString(ExchangeRatePreferences.serializer(), value)
        return localPreferenceSource.saveOrUpdate(Keys.EXCHANGE_RATE, exchangeRateJson)
    }

    override suspend fun deleteExchangeRate() {
        localPreferenceSource.delete(Keys.EXCHANGE_RATE)
    }
}