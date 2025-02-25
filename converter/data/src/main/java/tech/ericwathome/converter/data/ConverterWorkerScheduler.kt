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
import tech.ericwathome.core.domain.ConverterScheduler
import tech.ericwathome.core.domain.SessionStorage
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.toJavaDuration

class ConverterWorkerScheduler(
    context: Context,
    private val sessionStorage: SessionStorage,
) : ConverterScheduler {
    private val workManager = WorkManager.getInstance(context)

    override suspend fun scheduleSync(syncType: ConverterScheduler.SyncType) {
        when (syncType) {
            is ConverterScheduler.SyncType.FetchCurrencyMetaData -> {
                fetchCurrencyMetaData(syncType.duration)
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

        val elapsedTime =
            System.currentTimeMillis() - sessionStorage.lastMetadataSyncTimestamp()

        if (elapsedTime < duration.inWholeMilliseconds) {
            return
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
                backoffDelay = SyncCurrencyMetaDataWorker.DELAY,
                timeUnit = TimeUnit.MILLISECONDS,
            ).addTag(SyncCurrencyMetaDataWorker.TAG)
                .setInitialDelay(30, TimeUnit.MINUTES)
                .build()

        workManager.enqueue(workRequest)
    }

    override suspend fun cancelAllWork() {
        workManager.cancelAllWork().await()
    }
}