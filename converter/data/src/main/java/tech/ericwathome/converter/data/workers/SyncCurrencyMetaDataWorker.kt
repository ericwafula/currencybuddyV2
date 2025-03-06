@file:Keep

package tech.ericwathome.converter.data.workers

import android.content.Context
import androidx.annotation.Keep
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import tech.ericwathome.core.data.util.toWorkerResult
import tech.ericwathome.core.domain.converter.ConverterRepository
import tech.ericwathome.core.domain.converter.LocalConverterDataSource
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

fun Context.startOneTimeSyncCurrencyMetaDataWork(
    withInitialDelay: Boolean = false,
    lastSyncDurationMillis: Long = 0,
) {
    val timeElapsed = System.currentTimeMillis() - lastSyncDurationMillis
    val remainingTime =
        (SyncCurrencyMetaDataWorker.initialDelayDurationMillis - timeElapsed)
            .coerceAtLeast(0)

    val workRequest =
        OneTimeWorkRequestBuilder<SyncCurrencyMetaDataWorker>()
            .addTag(SyncCurrencyMetaDataWorker.TAG)
            .setInitialDelay(
                duration = if (withInitialDelay) remainingTime else 0,
                timeUnit = TimeUnit.MILLISECONDS,
            )
            .setConstraints(
                Constraints(
                    requiredNetworkType = NetworkType.CONNECTED,
                ),
            ).setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = SyncCurrencyMetaDataWorker.initialDelayDurationMillis,
                timeUnit = TimeUnit.MILLISECONDS,
            )
            .build()

    WorkManager.getInstance(this).enqueue(workRequest)
}

fun Context.startPeriodicSyncCurrencyMetaDataWork(duration: Duration) {
    val workRequest =
        PeriodicWorkRequestBuilder<SyncCurrencyMetaDataWorker>(
            repeatInterval = duration.toJavaDuration(),
        ).addTag(SyncCurrencyMetaDataWorker.TAG)
            .setConstraints(
                Constraints(
                    requiredNetworkType = NetworkType.CONNECTED,
                ),
            ).setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = SyncCurrencyMetaDataWorker.backoffDelayDurationMillis,
                timeUnit = TimeUnit.MILLISECONDS,
            ).build()

    WorkManager.getInstance(this).enqueue(workRequest)
}

class SyncCurrencyMetaDataWorker(
    context: Context,
    params: WorkerParameters,
    private val repository: ConverterRepository,
    private val localConverterDataSource: LocalConverterDataSource,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }

        localConverterDataSource.setIsMetadataSyncing(true)

        return when (val result = repository.syncCurrencyMetadata()) {
            is tech.ericwathome.core.domain.util.Result.Success -> {
                localConverterDataSource.setIsMetadataSyncing(false)
                Result.success()
            }

            is tech.ericwathome.core.domain.util.Result.Error -> {
                localConverterDataSource.setIsMetadataSyncing(false)
                result.error.toWorkerResult()
            }
        }
    }

    companion object {
        const val TAG = "sync_currency_metadata_worker_tag"
        val backoffDelayDurationMillis = 30.minutes.inWholeMilliseconds
        val initialDelayDurationMillis = 30.minutes.inWholeMilliseconds
    }
}