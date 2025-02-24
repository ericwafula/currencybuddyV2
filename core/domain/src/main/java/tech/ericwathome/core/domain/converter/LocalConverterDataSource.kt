package tech.ericwathome.core.domain.converter

import kotlinx.coroutines.flow.Flow
import tech.ericwathome.core.domain.converter.model.CurrencyDetails
import tech.ericwathome.core.domain.converter.model.ExchangeRate
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.EmptyResult

interface LocalConverterDataSource {
    fun getSelectedSavedExchangeRate(): Flow<ExchangeRate>

    fun getSavedExchangeRates(): Flow<List<ExchangeRate>>

    suspend fun getSavedExchangedRatesList(): List<ExchangeRate>

    suspend fun upsertToSavedExchangeRates(exchangeRate: ExchangeRate): EmptyResult<DataError.Local>

    suspend fun removeFromSavedExchangeRates(exchangeRate: ExchangeRate)

    suspend fun clearAllSavedExchangeRates()

    fun getCurrencyDetails(): Flow<List<CurrencyDetails>>

    fun queryCurrencyDetails(query: String): Flow<List<CurrencyDetails>>

    suspend fun upsertCurrencyDetails(
        currencyDetails: CurrencyDetails,
        rate: Double,
    ): EmptyResult<DataError.Local>

    suspend fun upsertCurrencyDetailsList(currencyDetailsList: List<CurrencyDetails>): EmptyResult<DataError.Local>

    suspend fun clearAllCurrencyDetails()
}