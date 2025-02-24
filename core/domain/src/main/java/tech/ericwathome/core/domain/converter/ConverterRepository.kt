package tech.ericwathome.core.domain.converter

import kotlinx.coroutines.flow.Flow
import tech.ericwathome.core.domain.converter.model.CurrencyDetails
import tech.ericwathome.core.domain.converter.model.ExchangeRate
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.EmptyResult

interface ConverterRepository {
    suspend fun fetchExchangeRate(
        fromCurrencyCode: String,
        toCurrencyCode: String,
        amount: Double,
        isSelected: Boolean,
    ): EmptyResult<DataError>

    fun getExchangeRate(): Flow<ExchangeRate>

    fun getSavedExchangeRates(): Flow<List<ExchangeRate>>

    fun getCurrencyDetails(): Flow<List<CurrencyDetails>>

    suspend fun fetchCurrencyDetails(): EmptyResult<DataError>

    suspend fun deleteExchangeRate(exchangeRate: ExchangeRate)

    suspend fun clearAllSavedExchangeRates()

    suspend fun clearAllCurrencyDetails()

    suspend fun syncSavedExchangeRates()
}