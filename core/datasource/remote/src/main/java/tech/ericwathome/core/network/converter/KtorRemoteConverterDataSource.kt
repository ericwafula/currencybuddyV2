package tech.ericwathome.core.network.converter

import io.ktor.client.HttpClient
import tech.ericwathome.core.data.network.get
import tech.ericwathome.core.domain.converter.RemoteConverterDataSource
import tech.ericwathome.core.domain.converter.model.CurrencyMetadata
import tech.ericwathome.core.domain.converter.model.ExchangeRate
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.Result
import tech.ericwathome.core.domain.util.map
import tech.ericwathome.core.network.BuildConfig
import tech.ericwathome.core.network.converter.dto.CurrencyMetadataDto
import tech.ericwathome.core.network.converter.dto.ExchangeRatesDto
import tech.ericwathome.core.network.converter.mappers.toDomain

class KtorRemoteConverterDataSource(
    private val httpClient: HttpClient,
) : RemoteConverterDataSource {
    override suspend fun getExchangeRate(
        base: String,
        quote: String,
        amount: Double,
    ): Result<ExchangeRate, DataError.Network> {
        val exchangeRatesResult =
            httpClient.get<ExchangeRatesDto>(
                route = "$base.json",
            )

        if (exchangeRatesResult is Result.Success) {
            val exchangeRate =
                exchangeRatesResult.data.rates[base]?.get(quote) ?: return Result.Error(
                    DataError.Network.UNKNOWN,
                )

            return Result.Success(
                exchangeRatesResult.data.toDomain(
                    base = base,
                    quote = quote,
                    rate = exchangeRate,
                    amount = amount,
                ),
            )
        }

        return exchangeRatesResult.map {
            it.toDomain(
                base = base,
                quote = quote,
                rate = 0.0,
                amount = 0.0,
            )
        }
    }

    override suspend fun fetchCurrencyMetadata(): Result<List<CurrencyMetadata>, DataError.Network> {
        return httpClient.get<List<CurrencyMetadataDto>>(
            route = BuildConfig.CURRENCY_DETAILS_URL,
        ).map { it.map { currencyMetadata -> currencyMetadata.toDomain() } }
    }
}