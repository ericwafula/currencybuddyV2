package tech.ericwathome.core.domain.converter

import tech.ericwathome.core.domain.converter.model.CurrencyPair
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.EmptyResult

typealias CurrencyCode = String

interface LocalConverterDataSource {
    suspend fun getFavouriteCurrencies(): List<CurrencyPair>

    suspend fun upsertToFavourites(currencyPair: CurrencyPair): EmptyResult<DataError.Local>

    suspend fun removeFromFavourites(currencyPair: CurrencyPair)

    suspend fun clearAllFavourites()
}