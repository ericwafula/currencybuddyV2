package tech.ericwathome.core.domain

import kotlin.time.Duration

interface ConverterScheduler {
    sealed interface SyncType {
        data class FetchCurrencyMetaData(val duration: Duration) : SyncType
    }

    suspend fun scheduleSync(syncType: SyncType)

    suspend fun cancelAllWork()
}