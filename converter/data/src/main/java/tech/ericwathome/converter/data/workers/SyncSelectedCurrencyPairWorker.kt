package tech.ericwathome.converter.data.workers

import android.content.Context
import android.content.Intent
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import tech.ericwathome.core.data.util.toWorkerResult
import tech.ericwathome.core.domain.SyncEventManager
import tech.ericwathome.core.domain.converter.ConverterRepository
import tech.ericwathome.core.domain.converter.LocalConverterDataSource
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

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

    WorkManager.getInstance(this).enqueueUniquePeriodicWork(
        uniqueWorkName = SyncSelectedCurrencyPairWorker.TAG,
        existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.KEEP,
        request = workRequest,
    )
}

class SyncSelectedCurrencyPairWorker(
    private val context: Context,
    params: WorkerParameters,
    private val converterRepository: ConverterRepository,
    private val localConverterDataSource: LocalConverterDataSource,
    private val syncEventManager: SyncEventManager,
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
                sendUpdateWidgetBroadcast(context)
                Result.success()
            }
        }
    }

    private fun sendUpdateWidgetBroadcast(context: Context) {
        val intent = Intent("tech.ericwathome.currencybuddy.broadcasts.ACTION_UPDATE_WIDGET")
        context.sendBroadcast(
            intent,
            "tech.ericwathome.currencybuddy.permission.UPDATE_WIDGET",
        )
    }

    companion object {
        const val TAG = "sync_exchange_rates_worker_tag"
        val backoffDelayMillis = 30.minutes.inWholeMilliseconds
        val initialDelayDurationMillis = 30.minutes.inWholeMilliseconds
    }
}