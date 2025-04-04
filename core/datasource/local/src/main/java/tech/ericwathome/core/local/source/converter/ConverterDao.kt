package tech.ericwathome.core.local.source.converter

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import tech.ericwathome.core.local.model.entity.CurrencyMetadataEntity
import tech.ericwathome.core.local.model.entity.ExchangeRateEntity

@Dao
interface ConverterDao {
    /**
     * Observes exchange rates that have not been marked as selected.
     *
     * @return A Flow emitting lists of non‑selected [ExchangeRateEntity] objects.
     */
    @Query("SELECT * FROM exchangerateentity")
    fun observeSavedExchangeRates(): Flow<List<ExchangeRateEntity>>

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
     * @return A Flow emitting lists of [CurrencyMetadataEntity] objects.
     */
    @Query("SELECT * FROM currencymetadataentity ORDER BY name ASC")
    fun observeCurrencyMetadata(): Flow<List<CurrencyMetadataEntity>>

    /**
     * Observes currency details filtered by a search query.
     *
     * The function returns a Flow emitting lists of [CurrencyMetadataEntity] objects whose name or code contains the specified query (case-insensitive).
     *
     * @param query The search string used to filter currency details.
     * @return A Flow emitting matching [CurrencyMetadataEntity] objects.
     */
    @Query(
        """
    SELECT * FROM currencymetadataentity 
    WHERE name LIKE '%' || :query || '%' COLLATE NOCASE 
       OR code LIKE '%' || :query || '%' COLLATE NOCASE
    """,
    )
    fun observeFilteredCurrencyMetadata(query: String): Flow<List<CurrencyMetadataEntity>>

    /**
     * Inserts or updates the provided currency details in the local database.
     *
     * @param currencyDetails The [CurrencyMetadataEntity] to be inserted or updated.
     */
    @Upsert
    suspend fun upsertLocalCurrencyMetadata(currencyDetails: CurrencyMetadataEntity)

    /**
     * Inserts or updates a list of currency details in the local database.
     *
     * @param currencyDetailsList The list of [CurrencyMetadataEntity] objects to be inserted or updated.
     */
    @Upsert
    suspend fun upsertLocalCurrencyMetadataList(currencyDetailsList: List<CurrencyMetadataEntity>)

    /**
     * Clears all currency details from the local database.
     *
     * This function deletes every record in the [CurrencyMetadataEntity] table.
     */
    @Query("DELETE FROM currencymetadataentity")
    suspend fun clearLocalCurrencyMetadata()
}