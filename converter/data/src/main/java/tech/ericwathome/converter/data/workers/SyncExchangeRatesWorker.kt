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
import tech.ericwathome.core.domain.converter.ConverterRepository
import tech.ericwathome.core.domain.converter.LocalConverterDataSource
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

fun Context.startOneTimeSyncExchangeRatesWork(
    withInitialDelay: Boolean = false,
    lastSyncDurationMillis: Long = 0,
) {
    val timeElapsed = System.currentTimeMillis() - lastSyncDurationMillis
    val remainingTime =
        (SyncExchangeRatesWorker.initialDelayDurationMillis - timeElapsed)
            .coerceAtLeast(0)

    val workRequest =
        OneTimeWorkRequestBuilder<SyncExchangeRatesWorker>()
            .addTag(SyncExchangeRatesWorker.TAG)
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
                backoffDelay = SyncExchangeRatesWorker.backoffDelayMillis,
                timeUnit = TimeUnit.MILLISECONDS,
            )
            .build()

    WorkManager.getInstance(this).enqueue(workRequest)
}

fun Context.startPeriodicSyncExchangeRatesWork(duration: Duration) {
    val workRequest =
        PeriodicWorkRequestBuilder<SyncExchangeRatesWorker>(
            repeatInterval = duration.toJavaDuration(),
        ).addTag(SyncExchangeRatesWorker.TAG)
            .setConstraints(
                Constraints(
                    requiredNetworkType = NetworkType.CONNECTED,
                ),
            ).setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = SyncExchangeRatesWorker.backoffDelayMillis,
                timeUnit = TimeUnit.MILLISECONDS,
            ).build()

    WorkManager.getInstance(this).enqueue(workRequest)
}

class SyncExchangeRatesWorker(
    context: Context,
    params: WorkerParameters,
    private val converterRepository: ConverterRepository,
    private val localConverterDataSource: LocalConverterDataSource,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }

        localConverterDataSource.setIsExchangeRateSyncing(true)

        return when (val result = converterRepository.syncSavedExchangeRates()) {
            is tech.ericwathome.core.domain.util.Result.Error -> {
                localConverterDataSource.setIsExchangeRateSyncing(false)
                result.error.toWorkerResult()
            }
            is tech.ericwathome.core.domain.util.Result.Success -> {
                localConverterDataSource.setIsExchangeRateSyncing(false)
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