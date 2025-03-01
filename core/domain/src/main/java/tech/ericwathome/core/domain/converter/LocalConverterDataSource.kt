package tech.ericwathome.core.domain.converter

import kotlinx.coroutines.flow.Flow
import tech.ericwathome.core.domain.converter.model.CurrencyMetadata
import tech.ericwathome.core.domain.converter.model.ExchangeRate
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.EmptyResult

interface LocalConverterDataSource {
    fun observeDefaultExchangeRate(): Flow<ExchangeRate>

    fun observeNonDefaultExchangeRates(): Flow<List<ExchangeRate>>

    suspend fun retrieveSavedExchangeRates(): List<ExchangeRate>

    suspend fun upsertLocalExchangeRate(exchangeRate: ExchangeRate): EmptyResult<DataError.Local>

    suspend fun deleteLocalExchangeRate(exchangeRate: ExchangeRate)

    suspend fun clearLocalExchangeRates()

    fun observeCurrencyMetadata(): Flow<List<CurrencyMetadata>>

    fun observeFilteredCurrencyMetaData(query: String): Flow<List<CurrencyMetadata>>

    suspend fun upsertLocalCurrencyMetaData(
        currencyMetaData: CurrencyMetadata,
        rate: Double,
    ): EmptyResult<DataError.Local>

    suspend fun upsertLocalCurrencyMetaDataList(currencyMetadataList: List<CurrencyMetadata>): EmptyResult<DataError.Local>

    suspend fun clearLocalCurrencyMetadata()
}