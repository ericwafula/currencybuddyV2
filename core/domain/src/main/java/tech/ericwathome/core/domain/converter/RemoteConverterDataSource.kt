package tech.ericwathome.core.domain.converter

import tech.ericwathome.core.domain.converter.model.CurrencyMetadata
import tech.ericwathome.core.domain.converter.model.ExchangeRate
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.Result

interface RemoteConverterDataSource {
    suspend fun getExchangeRate(
        base: String,
        quote: String,
        amount: Double,
    ): Result<ExchangeRate, DataError.Network>

    suspend fun fetchCurrencyMetadata(): Result<List<CurrencyMetadata>, DataError.Network>
}