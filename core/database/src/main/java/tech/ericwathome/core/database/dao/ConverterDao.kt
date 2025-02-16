package tech.ericwathome.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import tech.ericwathome.core.database.entity.CurrencyPairEntity

@Dao
interface ConverterDao {

    @Query("SELECT * FROM currencypairentity")
    suspend fun getFavouriteCurrencies(): List<CurrencyPairEntity>

    @Upsert
    suspend fun upsertToFavourites(currencyPair: CurrencyPairEntity)

    @Delete
    suspend fun removeFromFavourites(currencyPair: CurrencyPairEntity)

    @Query("DELETE FROM currencypairentity")
    suspend fun clearAllFavourites()

}