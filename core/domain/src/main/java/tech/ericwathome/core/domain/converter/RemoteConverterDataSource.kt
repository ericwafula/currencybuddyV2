package tech.ericwathome.core.domain.converter

import tech.ericwathome.core.domain.converter.model.CurrencyDetails
import tech.ericwathome.core.domain.converter.model.ExchangeRate
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.Result

interface RemoteConverterDataSource {
    suspend fun getExchangeRate(
        base: String,
        quote: String,
        amount: Double
    ): Result<ExchangeRate, DataError.Network>

    suspend fun getCurrencyDetails(): Result<List<CurrencyDetails>, DataError.Network>
}