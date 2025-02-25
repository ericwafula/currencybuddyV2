package tech.ericwathome.core.domain.converter

import kotlinx.coroutines.flow.Flow
import tech.ericwathome.core.domain.converter.model.CurrencyMetaData
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

    fun observeCurrencyMetaData(): Flow<List<CurrencyMetaData>>

    fun observeFilteredCurrencyMetaData(query: String): Flow<List<CurrencyMetaData>>

    suspend fun upsertLocalCurrencyMetaData(
        currencyMetaData: CurrencyMetaData,
        rate: Double,
    ): EmptyResult<DataError.Local>

    suspend fun upsertLocalCurrencyMetaDataList(currencyMetaDataList: List<CurrencyMetaData>): EmptyResult<DataError.Local>

    suspend fun clearLocalCurrencyMetaData()
}