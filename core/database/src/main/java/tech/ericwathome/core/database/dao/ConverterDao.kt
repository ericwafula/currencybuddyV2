package tech.ericwathome.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import tech.ericwathome.core.database.entity.CurrencyDetailsEntity
import tech.ericwathome.core.database.entity.ExchangeRateEntity

@Dao
interface ConverterDao {
    // Handle exchange rates
    @Query("SELECT * FROM exchangerateentity where isSelected = 1 LIMIT 1")
    fun getSelectedSavedExchangeRate(): Flow<ExchangeRateEntity>

    @Query("SELECT * FROM exchangerateentity where isSelected != 1")
    fun getSavedExchangeRates(): Flow<List<ExchangeRateEntity>>

    @Query("SELECT * FROM exchangerateentity where isSelected != 1")
    suspend fun getSavedExchangedRatesList(): List<ExchangeRateEntity>

    @Upsert
    suspend fun upsertToSavedExchangeRates(exchangeRate: ExchangeRateEntity)

    @Delete
    suspend fun removeFromSavedExchangeRates(exchangeRate: ExchangeRateEntity)

    @Query("DELETE FROM exchangerateentity")
    suspend fun clearAllSavedExchangeRates()

    // Handle currency details
    @Query("SELECT * FROM currencydetailsentity")
    fun getCurrencyDetails(): Flow<List<CurrencyDetailsEntity>>

    @Query(
        """
    SELECT * FROM currencydetailsentity 
    WHERE name LIKE '%' || :query || '%' COLLATE NOCASE 
       OR code LIKE '%' || :query || '%' COLLATE NOCASE
    """,
    )
    fun queryCurrencyDetails(query: String): Flow<List<CurrencyDetailsEntity>>

    @Upsert
    suspend fun upsertCurrencyDetails(currencyDetails: CurrencyDetailsEntity)

    @Upsert
    suspend fun upsertCurrencyDetailsList(currencyDetailsList: List<CurrencyDetailsEntity>)

    @Query("DELETE FROM currencydetailsentity")
    suspend fun clearAllCurrencyDetails()
}