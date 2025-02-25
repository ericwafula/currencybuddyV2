package tech.ericwathome.core.domain.converter

import kotlinx.coroutines.flow.Flow
import tech.ericwathome.core.domain.converter.model.CurrencyDetails
import tech.ericwathome.core.domain.converter.model.ExchangeRate
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.EmptyResult

interface LocalConverterDataSource {
    fun observeSelectedExchangeRate(): Flow<ExchangeRate>

    fun observeNonSelectedExchangeRates(): Flow<List<ExchangeRate>>

    suspend fun retrieveSavedExchangeRates(): List<ExchangeRate>

    suspend fun upsertLocalExchangeRate(exchangeRate: ExchangeRate): EmptyResult<DataError.Local>

    suspend fun deleteLocalExchangeRate(exchangeRate: ExchangeRate)

    suspend fun clearLocalExchangeRates()

    fun observeCurrencyDetails(): Flow<List<CurrencyDetails>>

    fun observeFilteredCurrencyDetails(query: String): Flow<List<CurrencyDetails>>

    suspend fun upsertLocalCurrencyDetails(
        currencyDetails: CurrencyDetails,
        rate: Double,
    ): EmptyResult<DataError.Local>

    suspend fun upsertLocalCurrencyDetailsList(currencyDetailsList: List<CurrencyDetails>): EmptyResult<DataError.Local>

    suspend fun clearLocalCurrencyDetails()
}