package tech.ericwathome.converter.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import tech.ericwathome.core.data.util.toWorkerResult
import tech.ericwathome.core.domain.converter.ConverterRepository

class SyncExchangeRatesWorker(
    context: Context,
    params: WorkerParameters,
    private val converterRepository: ConverterRepository,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return when (val result = converterRepository.syncSavedExchangeRates()) {
            is tech.ericwathome.core.domain.util.Result.Error -> {
                result.error.toWorkerResult()
            }
            is tech.ericwathome.core.domain.util.Result.Success -> Result.success()
        }
    }

    companion object {
        const val TAG = "sync_exchange_rates_worker_tag"
        const val BACKOFF_DELAY = 2_000L
    }
}