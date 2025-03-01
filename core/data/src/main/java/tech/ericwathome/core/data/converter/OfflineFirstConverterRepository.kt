package tech.ericwathome.core.data.converter

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tech.ericwathome.core.domain.SessionStorage
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

class OfflineFirstConverterRepository(
    private val remoteConverterDataSource: RemoteConverterDataSource,
    private val localConverterDataSource: LocalConverterDataSource,
    private val dispatchers: DispatcherProvider,
    private val applicationScope: CoroutineScope,
    private val sessionStorage: SessionStorage,
) : ConverterRepository {
    override suspend fun fetchExchangeRate(
        fromCurrencyCode: String,
        toCurrencyCode: String,
        amount: Double,
        isDefault: Boolean,
    ): EmptyResult<DataError> {
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
                    localConverterDataSource.upsertLocalExchangeRate(
                        result.data.copy(
                            isDefault = isDefault,
                            amount = amount,
                        ),
                    )
                }.await()
            }

            is Result.Error -> result.asEmptyDataResult()
        }
    }

    override fun observeSelectedExchangeRate(): Flow<ExchangeRate> {
        return localConverterDataSource.observeDefaultExchangeRate()
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
                    sessionStorage.setLastMetadataSyncTimestamp(System.currentTimeMillis())
                    syncResult
                }.await()
            }
        }
    }

    override fun observeNonSelectedExchangeRates(): Flow<List<ExchangeRate>> {
        return localConverterDataSource.observeNonDefaultExchangeRates()
    }

    override fun observeCurrencyMetadata(): Flow<List<CurrencyMetadata>> {
        return localConverterDataSource.observeCurrencyMetadata()
    }

    override suspend fun deleteLocalExchangeRate(exchangeRate: ExchangeRate) {
        localConverterDataSource.deleteLocalExchangeRate(exchangeRate)
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
                savedExchangeRates.map { exchangeRate ->
                    async {
                        when (
                            val result =
                                remoteConverterDataSource.getExchangeRate(
                                    base = exchangeRate.baseCode,
                                    quote = exchangeRate.targetCode,
                                    amount = if (exchangeRate.isDefault) exchangeRate.amount else 1.0,
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

            sessionStorage.setLastExchangeRateSyncTimestamp(System.currentTimeMillis())
            Result.Success(Unit).asEmptyDataResult()
        }
    }
}