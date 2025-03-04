package tech.ericwathome.converter.data

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tech.ericwathome.converter.data.workers.SyncCurrencyMetaDataWorker
import tech.ericwathome.converter.data.workers.SyncExchangeRatesWorker
import tech.ericwathome.core.domain.ConverterScheduler
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.toJavaDuration

class ConverterWorkerScheduler(
    context: Context,
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

    private suspend fun fetchCurrencyMetaData(duration: Duration) {
        val isSyncing =
            withContext(Dispatchers.IO) {
                workManager
                    .getWorkInfosByTag(SyncCurrencyMetaDataWorker.TAG)
                    .get()
                    .isNotEmpty()
            }

        if (isSyncing) {
            return
        }

        val workRequest =
            PeriodicWorkRequestBuilder<SyncCurrencyMetaDataWorker>(
                repeatInterval = duration.toJavaDuration(),
            ).setConstraints(
                Constraints(
                    requiredNetworkType = NetworkType.CONNECTED,
                ),
            ).setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = SyncCurrencyMetaDataWorker.backoffDelayMillis,
                timeUnit = TimeUnit.MILLISECONDS,
            ).addTag(SyncCurrencyMetaDataWorker.TAG)
                .setInitialDelay(
                    SyncCurrencyMetaDataWorker.initialDelayDurationMillis,
                    TimeUnit.MINUTES,
                )
                .build()

        workManager.enqueue(workRequest)
    }

    private suspend fun fetchExchangeRates(duration: Duration) {
        val isSyncing =
            withContext(Dispatchers.IO) {
                workManager
                    .getWorkInfosByTag(SyncExchangeRatesWorker.TAG)
                    .get()
                    .isNotEmpty()
            }

        if (isSyncing) {
            return
        }

        val workRequest =
            PeriodicWorkRequestBuilder<SyncExchangeRatesWorker>(
                repeatInterval = duration.toJavaDuration(),
            ).setConstraints(
                Constraints(
                    requiredNetworkType = NetworkType.CONNECTED,
                ),
            ).setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = SyncExchangeRatesWorker.backoffDelayMillis,
                timeUnit = TimeUnit.MILLISECONDS,
            ).addTag(SyncExchangeRatesWorker.TAG)
                .setInitialDelay(
                    SyncExchangeRatesWorker.initialDelayDurationMillis,
                    TimeUnit.MINUTES,
                )
                .build()

        workManager.enqueue(workRequest)
    }

    override suspend fun cancelAllWork() {
        workManager.cancelAllWork().await()
    }
}