package tech.ericwathome.converter.data

import android.content.Context
import androidx.work.WorkManager
import androidx.work.await
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import tech.ericwathome.converter.data.workers.SyncCurrencyMetaDataWorker
import tech.ericwathome.converter.data.workers.SyncSelectedCurrencyPairWorker
import tech.ericwathome.converter.data.workers.startPeriodicSyncSelectedCurrencyPairWork
import tech.ericwathome.converter.data.workers.startSyncCurrencyMetaDataWork
import tech.ericwathome.core.domain.converter.ConverterRepository
import tech.ericwathome.core.domain.converter.ConverterScheduler
import tech.ericwathome.core.domain.util.DispatcherProvider
import timber.log.Timber
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
                fetchCurrencyMetaData()
            }

            is ConverterScheduler.SyncType.FetchExchangeRate -> {
                fetchExchangeRates(syncType.duration, syncType.withInitialDelay)
            }
        }
    }

    private suspend fun fetchCurrencyMetaData() {
        val lastSyncTimestamp = converterRepository.lastMetadataSyncTimestampObservable.firstOrNull() ?: 0
        val isSyncing =
            withContext(dispatchers.io) {
                workManager
                    .getWorkInfosByTag(SyncCurrencyMetaDataWorker.TAG)
                    .get()
                    .isNotEmpty()
            }

        if (isSyncing) {
            return
        }

        context.startSyncCurrencyMetaDataWork(withInitialDelay = true, lastSyncDurationMillis = lastSyncTimestamp)
    }

    private suspend fun fetchExchangeRates(
        duration: Duration,
        withInitialDelay: Boolean,
    ) = coroutineScope {
        Timber.tag("ConverterWorkerScheduler").d("fetchExchangeRates: duration=$duration, withInitialDelay=$withInitialDelay")

        val lastSyncTimestamp = converterRepository.lastExchangeRateSyncTimestampObservable.firstOrNull() ?: 0
        val isSyncing =
            withContext(dispatchers.io) {
                workManager
                    .getWorkInfosByTag(SyncSelectedCurrencyPairWorker.TAG)
                    .get()
                    .isNotEmpty()
            }

        if (withInitialDelay && isSyncing) {
            return@coroutineScope
        }
        context.startPeriodicSyncSelectedCurrencyPairWork(duration)
    }

    override suspend fun cancelAllWork() {
        workManager.cancelAllWork().await()
    }
}