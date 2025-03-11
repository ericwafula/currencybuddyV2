package tech.ericwathome.core.data.converter

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tech.ericwathome.core.domain.converter.ConverterRepository
import tech.ericwathome.core.domain.converter.LocalConverterDataSource
import tech.ericwathome.core.domain.converter.RemoteConverterDataSource
import tech.ericwathome.core.domain.converter.model.CurrencyMetadata
import tech.ericwathome.core.domain.converter.model.ExchangeRate
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.DispatcherProvider
import tech.ericwathome.core.domain.util.EmptyResult
import tech.ericwathome.core.domain.util.Result
import tech.ericwathome.core.domain.util.asEmptyDataResult
import timber.log.Timber

internal class OfflineFirstConverterRepository(
    private val remoteConverterDataSource: RemoteConverterDataSource,
    private val localConverterDataSource: LocalConverterDataSource,
    private val dispatchers: DispatcherProvider,
    private val applicationScope: CoroutineScope,
) : ConverterRepository {
    override val savedExchangeRatesObservable: Flow<List<ExchangeRate>>
        get() = localConverterDataSource.savedExchangeRatesObservable

    override val currencyMetadataObservable: Flow<List<CurrencyMetadata>>
        get() = localConverterDataSource.observeCurrencyMetadata()

    override val lastMetadataSyncTimestampObservable: Flow<Long?>
        get() = localConverterDataSource.lastMetadataSyncTimestamp

    override val lastExchangeRateSyncTimestampObservable: Flow<Long?>
        get() = localConverterDataSource.lastExchangeRateSyncTimestamp

    override val isMetadataSyncingObservable: Flow<Boolean?>
        get() = localConverterDataSource.isMetadataSyncingObservable

    override val isExchangeRateSyncingObservable: Flow<Boolean?>
        get() = localConverterDataSource.isExchangeRateSyncingObservable

    override val exchangeRateObservable: Flow<ExchangeRate?>
        get() = localConverterDataSource.exchangeRateObservable

    override suspend fun fetchExchangeRate(
        fromCurrencyCode: String,
        toCurrencyCode: String,
        baseFlag: String?,
        quoteFlag: String?,
        amount: Double,
    ): EmptyResult<DataError> {
        if (amount == 0.0) {
            return Result.Success(Unit).asEmptyDataResult()
        }

        return when (
            val result =
                remoteConverterDataSource.getExchangeRate(
                    base = fromCurrencyCode,
                    quote = toCurrencyCode,
                    amount = amount,
                )
        ) {
            is Result.Success -> {
                applicationScope.async {
                    localConverterDataSource.setExchangeRate(
                        result.data.copy(
                            amount = amount,
                            baseFlag = baseFlag ?: "",
                            targetFlag = quoteFlag ?: "",
                        ),
                    )
                }.await()
            }

            is Result.Error -> result.asEmptyDataResult()
        }
    }

    override suspend fun syncCurrencyMetadata(): EmptyResult<DataError> {
        return when (
            val result = remoteConverterDataSource.fetchCurrencyMetadata()
        ) {
            is Result.Error -> result.asEmptyDataResult()
            is Result.Success -> {
                applicationScope.async {
                    val syncResult =
                        localConverterDataSource.upsertLocalCurrencyMetaDataList(result.data)
                    localConverterDataSource.setLastMetadataSyncTimestamp(System.currentTimeMillis())
                    syncResult
                }.await()
            }
        }
    }

    override fun observeFilteredCurrencyMetaData(query: String): Flow<List<CurrencyMetadata>> {
        return localConverterDataSource.observeFilteredCurrencyMetaData(query)
    }

    override suspend fun deleteExchangeRate() {
        localConverterDataSource.deleteExchangeRate()
    }

    override suspend fun clearLocalExchangeRates() {
        localConverterDataSource.clearLocalExchangeRates()
    }

    override suspend fun clearLocalCurrencyMetadata() {
        localConverterDataSource.clearLocalCurrencyMetadata()
    }

    override suspend fun syncSavedExchangeRates(): EmptyResult<DataError> {
        return withContext(dispatchers.io) {
            val savedExchangeRates = localConverterDataSource.retrieveSavedExchangeRates()
            val errorList = mutableListOf<DataError>()

            val syncJobs =
                savedExchangeRates.filter { exchangeRate -> exchangeRate.amount != 0.0 }.map { exchangeRate ->
                    async {
                        when (
                            val result =
                                remoteConverterDataSource.getExchangeRate(
                                    base = exchangeRate.baseCode,
                                    quote = exchangeRate.targetCode,
                                    amount = 1.0,
                                )
                        ) {
                            is Result.Error -> {
                                Timber.e("Failed to sync exchange rate: $exchangeRate")
                                errorList += result.error
                            }

                            is Result.Success -> {
                                applicationScope.launch {
                                    localConverterDataSource.upsertLocalExchangeRate(
                                        result.data,
                                    )
                                }.join()
                            }
                        }
                    }
                }

            syncJobs.awaitAll()

            if (errorList.isNotEmpty()) {
                Result.Error(errorList.first())
            }

            localConverterDataSource.setLastExchangeRateSyncTimestamp(System.currentTimeMillis())
            Result.Success(Unit).asEmptyDataResult()
        }
    }

    override suspend fun setExchangeRate(value: ExchangeRate): EmptyResult<DataError.Local> {
        return localConverterDataSource.setExchangeRate(value)
    }
}