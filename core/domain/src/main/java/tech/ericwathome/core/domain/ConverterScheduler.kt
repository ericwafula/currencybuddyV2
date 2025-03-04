package tech.ericwathome.core.domain

import kotlin.time.Duration

interface ConverterScheduler {
    sealed interface SyncType {
        data class FetchCurrencyMetadata(val duration: Duration) : SyncType

        data class FetchExchangeRates(val duration: Duration) : SyncType
    }

    suspend fun scheduleSync(syncType: SyncType)

    suspend fun cancelAllWork()
}