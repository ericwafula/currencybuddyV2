@file:Keep

package tech.ericwathome.converter.data.workers

import android.content.Context
import androidx.annotation.Keep
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
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
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.minutes

fun Context.startSyncCurrencyMetaDataWork(
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

class SyncCurrencyMetaDataWorker(
    context: Context,
    params: WorkerParameters,
    private val repository: ConverterRepository,
    private val localConverterDataSource: LocalConverterDataSource,
    private val notificationFactory: NotificationFactory,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }

        localConverterDataSource.setIsMetadataSyncing(true)

        return when (val result = repository.syncCurrencyMetadata()) {
            is tech.ericwathome.core.domain.util.Result.Success -> {
                localConverterDataSource.setIsMetadataSyncing(false)
                showSyncNotification(true)
                Result.success()
            }

            is tech.ericwathome.core.domain.util.Result.Error -> {
                localConverterDataSource.setIsMetadataSyncing(false)
                showSyncNotification(false)
                result.error.toWorkerResult()
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
                "Sync Success" to "Currencies synced successfully"
            } else {
                "Sync Error" to "Unable to sync currencies"
            }

        if (BuildConfig.DEBUG) {
            notification.show(title, message)
        }
    }

    companion object {
        const val TAG = "sync_currency_metadata_worker_tag"
        val initialDelayDurationMillis = 30.minutes.inWholeMilliseconds
    }
}