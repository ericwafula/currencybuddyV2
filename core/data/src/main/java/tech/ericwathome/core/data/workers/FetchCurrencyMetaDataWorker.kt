package tech.ericwathome.core.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import tech.ericwathome.core.data.util.toWorkerResult
import tech.ericwathome.core.domain.converter.ConverterRepository

class FetchCurrencyMetaDataWorker(
    context: Context,
    params: WorkerParameters,
    private val repository: ConverterRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount > 5) {
            return Result.failure()
        }

        return when (val result = repository.fetchCurrencyMetaData()) {
            is tech.ericwathome.core.domain.util.Result.Success -> Result.success()
            is tech.ericwathome.core.domain.util.Result.Error -> {
                result.error.toWorkerResult()
            }
        }
    }
}