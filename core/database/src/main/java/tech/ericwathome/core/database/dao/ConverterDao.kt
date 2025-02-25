package tech.ericwathome.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import tech.ericwathome.core.database.entity.CurrencyMetaDataEntity
import tech.ericwathome.core.database.entity.ExchangeRateEntity

@Dao
interface ConverterDao {
    /**
     * Observes the locally saved exchange rate that is marked as selected.
     *
     * @return A Flow emitting the selected [ExchangeRateEntity] object.
     */
    @Query("SELECT * FROM exchangerateentity where isSelected = 1 LIMIT 1")
    fun observeSelectedExchangeRate(): Flow<ExchangeRateEntity>

    /**
     * Observes exchange rates that have not been marked as selected.
     *
     * @return A Flow emitting lists of non‑selected [ExchangeRateEntity] objects.
     */
    @Query("SELECT * FROM exchangerateentity where isSelected != 1")
    fun observeNonSelectedExchangeRates(): Flow<List<ExchangeRateEntity>>

    /**
     * Retrieves a list of all locally saved exchange rates.
     *
     * @return A [List] of all [ExchangeRateEntity] objects stored in the database.
     */
    @Query("SELECT * FROM exchangerateentity")
    suspend fun retrieveSavedExchangeRates(): List<ExchangeRateEntity>

    /**
     * Inserts or updates the given exchange rate in the local database.
     *
     * @param exchangeRate The [ExchangeRateEntity] to be inserted or updated.
     */
    @Upsert
    suspend fun upsertLocalExchangeRate(exchangeRate: ExchangeRateEntity)

    /**
     * Deletes the specified exchange rate from the local saved exchange rates.
     *
     * @param exchangeRate The [ExchangeRateEntity] to be removed.
     */
    @Delete
    suspend fun deleteLocalExchangeRate(exchangeRate: ExchangeRateEntity)

    /**
     * Clears all exchange rates from the local saved exchange rates.
     *
     * This function deletes every record in the [ExchangeRateEntity] table.
     */
    @Query("DELETE FROM exchangerateentity")
    suspend fun clearLocalExchangeRates()

    /**
     * Observes the list of locally stored currency details.
     *
     * @return A Flow emitting lists of [CurrencyMetaDataEntity] objects.
     */
    @Query("SELECT * FROM currencymetadataentity")
    fun observeCurrencyMetaData(): Flow<List<CurrencyMetaDataEntity>>

    /**
     * Observes currency details filtered by a search query.
     *
     * The function returns a Flow emitting lists of [CurrencyMetaDataEntity] objects whose name or code contains the specified query (case-insensitive).
     *
     * @param query The search string used to filter currency details.
     * @return A Flow emitting matching [CurrencyMetaDataEntity] objects.
     */
    @Query(
        """
    SELECT * FROM currencymetadataentity 
    WHERE name LIKE '%' || :query || '%' COLLATE NOCASE 
       OR code LIKE '%' || :query || '%' COLLATE NOCASE
    """,
    )
    fun observeFilteredCurrencyMetaData(query: String): Flow<List<CurrencyMetaDataEntity>>

    /**
     * Inserts or updates the provided currency details in the local database.
     *
     * @param currencyDetails The [CurrencyMetaDataEntity] to be inserted or updated.
     */
    @Upsert
    suspend fun upsertLocalCurrencyMetaData(currencyDetails: CurrencyMetaDataEntity)

    /**
     * Inserts or updates a list of currency details in the local database.
     *
     * @param currencyDetailsList The list of [CurrencyMetaDataEntity] objects to be inserted or updated.
     */
    @Upsert
    suspend fun upsertLocalCurrencyMetaDataList(currencyDetailsList: List<CurrencyMetaDataEntity>)

    /**
     * Clears all currency details from the local database.
     *
     * This function deletes every record in the [CurrencyMetaDataEntity] table.
     */
    @Query("DELETE FROM currencymetadataentity")
    suspend fun clearLocalCurrencyMetaData()
}