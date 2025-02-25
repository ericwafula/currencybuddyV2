package tech.ericwathome.core.data.converter

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tech.ericwathome.core.domain.converter.ConverterRepository
import tech.ericwathome.core.domain.converter.LocalConverterDataSource
import tech.ericwathome.core.domain.converter.RemoteConverterDataSource
import tech.ericwathome.core.domain.converter.model.CurrencyDetails
import tech.ericwathome.core.domain.converter.model.ExchangeRate
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.DispatcherProvider
import tech.ericwathome.core.domain.util.EmptyResult
import tech.ericwathome.core.domain.util.Result
import tech.ericwathome.core.domain.util.asEmptyDataResult

class DefaultConverterRepository(
    private val remoteConverterDataSource: RemoteConverterDataSource,
    private val localConverterDataSource: LocalConverterDataSource,
    private val dispatchers: DispatcherProvider,
    private val applicationScope: CoroutineScope,
) : ConverterRepository {
    override suspend fun fetchExchangeRate(
        fromCurrencyCode: String,
        toCurrencyCode: String,
        amount: Double,
        isSelected: Boolean,
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
                            isSelected = isSelected,
                            amount = amount,
                        ),
                    )
                }.await()
            }

            is Result.Error -> result.asEmptyDataResult()
        }
    }

    override fun getExchangeRate(): Flow<ExchangeRate> {
        return localConverterDataSource.observeSelectedExchangeRate()
    }

    override suspend fun fetchCurrencyDetails(): EmptyResult<DataError> {
        return when (
            val result = remoteConverterDataSource.getCurrencyDetails()
        ) {
            is Result.Error -> result.asEmptyDataResult()
            is Result.Success -> {
                applicationScope.async {
                    localConverterDataSource.upsertLocalCurrencyDetailsList(result.data)
                }.await()
            }
        }
    }

    override fun getSavedExchangeRates(): Flow<List<ExchangeRate>> {
        return localConverterDataSource.observeNonSelectedExchangeRates()
    }

    override fun getCurrencyDetails(): Flow<List<CurrencyDetails>> {
        return localConverterDataSource.observeCurrencyDetails()
    }

    override suspend fun deleteExchangeRate(exchangeRate: ExchangeRate) {
        localConverterDataSource.deleteLocalExchangeRate(exchangeRate)
    }

    override suspend fun clearAllSavedExchangeRates() {
        localConverterDataSource.clearLocalExchangeRates()
    }

    override suspend fun clearAllCurrencyDetails() {
        localConverterDataSource.clearLocalCurrencyDetails()
    }

    override suspend fun syncSavedExchangeRates() {
        withContext(dispatchers.io) {
            val savedExchangeRates = localConverterDataSource.retrieveSavedExchangeRates()

            val syncJobs =
                savedExchangeRates.map { exchangeRate ->
                    launch {
                        when (
                            val result =
                                remoteConverterDataSource.getExchangeRate(
                                    base = exchangeRate.baseCode,
                                    quote = exchangeRate.targetCode,
                                    amount = if (exchangeRate.isSelected) exchangeRate.amount else 1.0,
                                )
                        ) {
                            is Result.Error -> Unit
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

            syncJobs.joinAll()
        }
    }
}