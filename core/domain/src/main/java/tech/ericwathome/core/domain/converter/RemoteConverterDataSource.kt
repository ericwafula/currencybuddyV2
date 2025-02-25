package tech.ericwathome.core.domain.converter

import tech.ericwathome.core.domain.converter.model.CurrencyMetaData
import tech.ericwathome.core.domain.converter.model.ExchangeRate
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.Result

interface RemoteConverterDataSource {
    suspend fun getExchangeRate(
        base: String,
        quote: String,
        amount: Double,
    ): Result<ExchangeRate, DataError.Network>

    suspend fun fetchCurrencyMetaData(): Result<List<CurrencyMetaData>, DataError.Network>
}