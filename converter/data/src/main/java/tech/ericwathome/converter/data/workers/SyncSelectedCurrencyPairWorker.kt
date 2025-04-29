package tech.ericwathome.converter.data.workers

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.flow.firstOrNull
import tech.ericwathome.converter.data.BuildConfig
import tech.ericwathome.core.data.util.toWorkerResult
import tech.ericwathome.core.domain.converter.ConverterRepository
import tech.ericwathome.core.domain.converter.LocalConverterDataSource
import tech.ericwathome.core.domain.notification.NotificationFactory
import tech.ericwathome.core.domain.notification.strategy.NotificationStrategy
import tech.ericwathome.core.domain.util.orFalse
import tech.ericwathome.core.domain.widget.ConverterWidgetUpdater
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
    context: Context,
    params: WorkerParameters,
    private val converterRepository: ConverterRepository,
    private val localConverterDataSource: LocalConverterDataSource,
    private val notificationFactory: NotificationFactory,
    private val converterWidgetUpdater: ConverterWidgetUpdater,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }

        localConverterDataSource.setIsSelectedCurrencyPairSyncing(true)

        return when (val result = converterRepository.syncSelectedCurrencyPair()) {
            is tech.ericwathome.core.domain.util.Result.Error -> {
                localConverterDataSource.setIsSelectedCurrencyPairSyncing(false)
                showSyncNotification(false)
                result.error.toWorkerResult()
            }

            is tech.ericwathome.core.domain.util.Result.Success -> {
                localConverterDataSource.setIsSelectedCurrencyPairSyncing(false)
                val exchangeRate = converterRepository.exchangeRateObservable.firstOrNull()
                exchangeRate?.let {
                    converterWidgetUpdater.update(it)
                }
                showSyncNotification(true)
                Result.success()
            }
        }
    }

    private suspend fun showSyncNotification(isSuccess: Boolean) {
        val hasNotificationPermission =
            localConverterDataSource.hasNotificationPermission.firstOrNull().orFalse()
        val notification =
            notificationFactory.get(NotificationStrategy.Type.SYNC)

        if (!hasNotificationPermission) return

        val (title, message) =
            if (isSuccess) {
                "Sync Success" to "Selected currency pair synced successfully"
            } else {
                "Sync Error" to "Unable to sync selected currency pair"
            }

        if (BuildConfig.DEBUG) {
            notification.show(title, message)
        }

    }

    companion object {
        const val TAG = "sync_exchange_rates_worker_tag"
        val backoffDelayMillis = 30.minutes.inWholeMilliseconds
        val initialDelayDurationMillis = 30.minutes.inWholeMilliseconds
    }
}