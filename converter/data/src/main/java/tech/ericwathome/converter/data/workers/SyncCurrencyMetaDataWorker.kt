package tech.ericwathome.converter.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import tech.ericwathome.core.data.util.toWorkerResult
import tech.ericwathome.core.domain.converter.ConverterRepository
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class SyncCurrencyMetaDataWorker(
    context: Context,
    params: WorkerParameters,
    private val repository: ConverterRepository,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }

        return when (val result = repository.syncCurrencyMetadata()) {
            is tech.ericwathome.core.domain.util.Result.Success -> {
                Result.success()
            }

            is tech.ericwathome.core.domain.util.Result.Error -> {
                result.error.toWorkerResult()
            }
        }
    }

    companion object {
        const val TAG = "fetch_currency_metadata_worker_tag"
        val backoffDelayMillis = 2.seconds.inWholeMilliseconds
        val initialDelayDurationMillis = 30.minutes.inWholeMilliseconds
    }
}