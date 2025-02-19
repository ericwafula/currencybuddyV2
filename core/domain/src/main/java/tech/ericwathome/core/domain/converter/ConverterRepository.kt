package tech.ericwathome.core.domain.converter

import tech.ericwathome.core.domain.converter.model.CurrencyDetails
import tech.ericwathome.core.domain.converter.model.CurrencyPair
import tech.ericwathome.core.domain.converter.model.ExchangeRate
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.EmptyResult
import tech.ericwathome.core.domain.util.Result

interface ConverterRepository {
    suspend fun getConversionRate(
        fromCurrencyCode: String,
        toCurrencyCode: String,
        amount: Double,
    ): Result<ExchangeRate, DataError.Network>

    suspend fun getCurrencyDetails(): Result<List<CurrencyDetails>, DataError.Network>

    suspend fun getFavouriteCurrencies(): Result<List<ExchangeRate>, DataError.Network>

    suspend fun upsertToFavourites(currencyPair: CurrencyPair): EmptyResult<DataError>

    suspend fun removeFromFavourites(currencyPair: CurrencyPair)

    suspend fun clearAllFavourites()
}