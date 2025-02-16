package tech.ericwathome.core.database

import android.database.sqlite.SQLiteFullException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tech.ericwathome.core.database.dao.ConverterDao
import tech.ericwathome.core.database.mappers.toCurrencyPair
import tech.ericwathome.core.database.mappers.toCurrencyPairEntity
import tech.ericwathome.core.domain.converter.CurrencyCode
import tech.ericwathome.core.domain.converter.LocalConverterDataSource
import tech.ericwathome.core.domain.converter.model.CurrencyPair
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.EmptyResult
import tech.ericwathome.core.domain.util.Result

class RoomLocalConverterDataSource(
    private val converterDao: ConverterDao
) : LocalConverterDataSource {
    override suspend fun getFavouriteCurrencies(): List<CurrencyPair> {
        return converterDao.getFavouriteCurrencies().map { currencyPairEntities ->
            currencyPairEntities.toCurrencyPair()
        }
    }

    override suspend fun upsertToFavourites(currencyPair: CurrencyPair): EmptyResult<DataError.Local> {
        return try {
            converterDao.upsertToFavourites(currencyPair.toCurrencyPairEntity())
            Result.Success(Unit)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun removeFromFavourites(currencyPair: CurrencyPair) {
        converterDao.removeFromFavourites(currencyPair.toCurrencyPairEntity())
    }

    override suspend fun clearAllFavourites() {
        converterDao.clearAllFavourites()
    }
}