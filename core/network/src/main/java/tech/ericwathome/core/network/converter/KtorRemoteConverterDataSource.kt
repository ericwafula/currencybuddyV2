package tech.ericwathome.core.network.converter

import io.ktor.client.HttpClient
import tech.ericwathome.core.data.network.get
import tech.ericwathome.core.domain.converter.RemoteConverterDataSource
import tech.ericwathome.core.domain.converter.model.CurrencyDetails
import tech.ericwathome.core.domain.converter.model.ExchangeRate
import tech.ericwathome.core.domain.util.CurrencyUtils
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.Result
import tech.ericwathome.core.domain.util.asEmptyDataResult
import tech.ericwathome.core.network.BuildConfig
import tech.ericwathome.core.network.converter.dto.ExchangeRatesDto

class KtorRemoteConverterDataSource(
    private val httpClient: HttpClient
) : RemoteConverterDataSource {
    override suspend fun getExchangeRate(
        base: String,
        quote: String,
        amount: Double
    ): Result<ExchangeRate, DataError.Network> {
        val exchangeRatesResult = httpClient.get<ExchangeRatesDto>(
            route = "$base.json"
        )

        if (exchangeRatesResult is Result.Success) {
            val exchangeRate = exchangeRatesResult.data.rates[base]?.get(quote) ?: return Result.Error(
                DataError.Network.UNKNOWN
            )
            val result = with(CurrencyUtils) { (exchangeRate * amount).roundToDecimalPlaces(2) }

            return Result.Success(
                ExchangeRate(
                    baseCode = base,
                    conversionRate = with(CurrencyUtils) { exchangeRate.roundToDecimalPlaces(2) },
                    conversionResult = result,
                    targetCode = quote
                )
            )
        }

        return exchangeRatesResult as Result.Error<DataError.Network>
    }

    override suspend fun getCurrencyDetails(): Result<List<CurrencyDetails>, DataError.Network> {
        return httpClient.get<List<CurrencyDetails>>(
            route = BuildConfig.CURRENCY_DETAILS_URL
        )
    }
}