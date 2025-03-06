package tech.ericwathome.converter.data

import android.content.Context
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.await
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tech.ericwathome.converter.data.workers.SyncCurrencyMetaDataWorker
import tech.ericwathome.converter.data.workers.SyncExchangeRatesWorker
import tech.ericwathome.converter.data.workers.startOneTimeSyncCurrencyMetaDataWork
import tech.ericwathome.converter.data.workers.startOneTimeSyncExchangeRatesWork
import tech.ericwathome.converter.data.workers.startPeriodicSyncCurrencyMetaDataWork
import tech.ericwathome.converter.data.workers.startPeriodicSyncExchangeRatesWork
import tech.ericwathome.core.domain.ConverterScheduler
import tech.ericwathome.core.domain.converter.ConverterRepository
import tech.ericwathome.core.domain.util.DispatcherProvider
import kotlin.time.Duration

class ConverterWorkerScheduler(
    private val context: Context,
    private val dispatchers: DispatcherProvider,
    private val converterRepository: ConverterRepository,
) : ConverterScheduler {
    private val workManager = WorkManager.getInstance(context)

    override suspend fun scheduleSync(syncType: ConverterScheduler.SyncType) {
        when (syncType) {
            is ConverterScheduler.SyncType.FetchCurrencyMetadata -> {
                fetchCurrencyMetaData(syncType.duration)
            }

            is ConverterScheduler.SyncType.FetchExchangeRates -> {
                fetchExchangeRates(syncType.duration)
            }
        }
    }

    private suspend fun fetchCurrencyMetaData(duration: Duration) =
        coroutineScope {
            val lastSyncTimestamp = converterRepository.lastMetadataSyncTimestampObservable.firstOrNull() ?: 0
            val isSyncing =
                withContext(dispatchers.io) {
                    workManager
                        .getWorkInfosByTag(SyncCurrencyMetaDataWorker.TAG)
                        .get()
                        .isNotEmpty()
                }

            if (isSyncing) {
                return@coroutineScope
            }

            context.startOneTimeSyncCurrencyMetaDataWork(withInitialDelay = true, lastSyncDurationMillis = lastSyncTimestamp)

            launch(dispatchers.io) {
                workManager
                    .getWorkInfosByTagFlow(SyncCurrencyMetaDataWorker.TAG)
                    .filter { workInfos -> workInfos.firstOrNull()?.state == WorkInfo.State.SUCCEEDED }
                    .take(1)
                    .collect { context.startPeriodicSyncCurrencyMetaDataWork(duration) }
            }
        }

    private suspend fun fetchExchangeRates(duration: Duration) =
        coroutineScope {
            val lastSyncTimestamp = converterRepository.lastExchangeRateSyncTimestampObservable.firstOrNull() ?: 0
            val isSyncing =
                withContext(dispatchers.io) {
                    workManager
                        .getWorkInfosByTag(SyncExchangeRatesWorker.TAG)
                        .get()
                        .isNotEmpty()
                }

            if (isSyncing) {
                return@coroutineScope
            }

            context.startOneTimeSyncExchangeRatesWork(withInitialDelay = true, lastSyncDurationMillis = lastSyncTimestamp)

            launch(dispatchers.io) {
                workManager
                    .getWorkInfosByTagFlow(SyncExchangeRatesWorker.TAG)
                    .filter { workInfos -> workInfos.firstOrNull()?.state == WorkInfo.State.SUCCEEDED }
                    .take(1)
                    .collect { context.startPeriodicSyncExchangeRatesWork(duration) }
            }
        }

    override suspend fun cancelAllWork() {
        workManager.cancelAllWork().await()
    }
}