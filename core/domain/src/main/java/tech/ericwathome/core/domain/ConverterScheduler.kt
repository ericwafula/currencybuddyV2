package tech.ericwathome.core.domain

import kotlin.time.Duration

interface ConverterScheduler {
    sealed interface SyncType {
        data object FetchCurrencyMetadata : SyncType

        data class FetchExchangeRate(val duration: Duration, val withInitialDelay: Boolean) : SyncType
    }

    suspend fun scheduleSync(syncType: SyncType)

    suspend fun cancelAllWork()
}