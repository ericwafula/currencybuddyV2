package tech.ericwathome.converter.data.workers

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import tech.ericwathome.core.data.util.toWorkerResult
import tech.ericwathome.core.domain.SyncEventManager
import tech.ericwathome.core.domain.converter.ConverterRepository
import tech.ericwathome.core.domain.converter.LocalConverterDataSource
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

fun Context.startOneTimeSyncSelectedCurrencyPairWork(
    withInitialDelay: Boolean = false,
    lastSyncDurationMillis: Long = 0,
) {
    val timeElapsed = System.currentTimeMillis() - lastSyncDurationMillis
    val remainingTime =
        (SyncSelectedCurrencyPairWorker.initialDelayDurationMillis - timeElapsed)
            .coerceAtLeast(0)
    val initialDelay = if (withInitialDelay) remainingTime else 0

    Timber.tag("SyncSelectedCurrencyPairWorker").d("remainingTime: $remainingTime, initialDelay: $initialDelay")

    val workRequest =
        OneTimeWorkRequestBuilder<SyncSelectedCurrencyPairWorker>()
            .addTag(SyncSelectedCurrencyPairWorker.TAG)
            .setInitialDelay(
                duration = if (withInitialDelay) remainingTime else 0,
                timeUnit = TimeUnit.MILLISECONDS,
            )
            .setConstraints(
                Constraints(
                    requiredNetworkType = NetworkType.CONNECTED,
                ),
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = SyncSelectedCurrencyPairWorker.backoffDelayMillis,
                timeUnit = TimeUnit.MILLISECONDS,
            )
            .build()

    WorkManager.getInstance(this).enqueue(workRequest)
}

fun Context.startPeriodicSyncSelectedCurrencyPairWork(duration: Duration) {
    val workRequest =
        PeriodicWorkRequestBuilder<SyncSelectedCurrencyPairWorker>(
            repeatInterval = duration.toJavaDuration(),
        ).addTag(SyncSelectedCurrencyPairWorker.TAG)
            .setConstraints(
                Constraints(
                    requiredNetworkType = NetworkType.CONNECTED,
                ),
            ).setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = SyncSelectedCurrencyPairWorker.backoffDelayMillis,
                timeUnit = TimeUnit.MILLISECONDS,
            ).build()

    WorkManager.getInstance(this).enqueue(workRequest)
}

class SyncSelectedCurrencyPairWorker(
    context: Context,
    params: WorkerParameters,
    private val converterRepository: ConverterRepository,
    private val localConverterDataSource: LocalConverterDataSource,
    private val syncEventManager: SyncEventManager
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }

        localConverterDataSource.setIsSelectedCurrencyPairSyncing(true)

        return when (val result = converterRepository.syncSelectedCurrencyPair()) {
            is tech.ericwathome.core.domain.util.Result.Error -> {
                localConverterDataSource.setIsSelectedCurrencyPairSyncing(false)
                syncEventManager.onEvent(SyncEventManager.SyncEvent.SyncSelectedCurrencyPairError)
                result.error.toWorkerResult()
            }
            is tech.ericwathome.core.domain.util.Result.Success -> {
                localConverterDataSource.setIsSelectedCurrencyPairSyncing(false)
                syncEventManager.onEvent(SyncEventManager.SyncEvent.SyncSelectedCurrencyPairSuccess)
                Result.success()
            }
        }
    }

    companion object {
        const val TAG = "sync_exchange_rates_worker_tag"
        val backoffDelayMillis = 30.minutes.inWholeMilliseconds
        val initialDelayDurationMillis = 30.minutes.inWholeMilliseconds
    }
}