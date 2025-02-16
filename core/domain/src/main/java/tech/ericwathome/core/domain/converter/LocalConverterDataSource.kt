package tech.ericwathome.core.domain.converter

import kotlinx.coroutines.flow.Flow
import tech.ericwathome.core.domain.converter.model.CurrencyPair
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.EmptyResult
import tech.ericwathome.core.domain.util.Result

typealias CurrencyCode = String

interface LocalConverterDataSource {
    suspend fun getFavouriteCurrencies(): List<CurrencyPair>

    suspend fun upsertToFavourites(currencyPair: CurrencyPair): EmptyResult<DataError.Local>

    suspend fun removeFromFavourites(currencyPair: CurrencyPair)

    suspend fun clearAllFavourites()
}