package tech.ericwathome.core.data.converter

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import tech.ericwathome.core.domain.converter.ConverterRepository
import tech.ericwathome.core.domain.converter.LocalConverterDataSource
import tech.ericwathome.core.domain.converter.RemoteConverterDataSource
import tech.ericwathome.core.domain.converter.model.CurrencyDetails
import tech.ericwathome.core.domain.converter.model.CurrencyPair
import tech.ericwathome.core.domain.converter.model.ExchangeRate
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.EmptyResult
import tech.ericwathome.core.domain.util.Result

class DefaultConverterRepository(
    private val remoteConverterDataSource: RemoteConverterDataSource,
    private val localConverterDataSource: LocalConverterDataSource,
) : ConverterRepository {
    override suspend fun getConversionRate(
        fromCurrencyCode: String,
        toCurrencyCode: String,
        amount: Double,
    ): Result<ExchangeRate, DataError.Network> {
        return remoteConverterDataSource.getExchangeRate(fromCurrencyCode, toCurrencyCode, amount)
    }

    override suspend fun getCurrencyDetails(): Result<List<CurrencyDetails>, DataError.Network> {
        return remoteConverterDataSource.getCurrencyDetails()
    }

    override suspend fun getFavouriteCurrencies(): Result<List<ExchangeRate>, DataError.Network> {
        return coroutineScope {
            val favouriteCurrencies = localConverterDataSource.getFavouriteCurrencies()

            val deferredResults =
                favouriteCurrencies.map { currencyPair ->
                    async {
                        remoteConverterDataSource.getExchangeRate(
                            currencyPair.baseCurrency.code,
                            currencyPair.quoteCurrency.code,
                            1.0,
                        )
                    }
                }

            val results = deferredResults.awaitAll()

            // Return first error if there is one
            val errorResult = results.filterIsInstance<Result.Error<DataError.Network>>().firstOrNull()
            if (errorResult != null) return@coroutineScope errorResult

            // Otherwise, return the successful results
            val exchangeRates = results.filterIsInstance<Result.Success<ExchangeRate>>().map { it.data }
            Result.Success(exchangeRates)
        }
    }

    override suspend fun upsertToFavourites(currencyPair: CurrencyPair): EmptyResult<DataError> {
        return localConverterDataSource.upsertToFavourites(currencyPair)
    }

    override suspend fun removeFromFavourites(currencyPair: CurrencyPair) {
        localConverterDataSource.removeFromFavourites(currencyPair)
    }

    override suspend fun clearAllFavourites() {
        localConverterDataSource.clearAllFavourites()
    }
}