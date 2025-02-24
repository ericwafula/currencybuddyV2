package tech.ericwathome.core.network.converter.dto

import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import tech.ericwathome.core.domain.converter.model.ExchangeRate
import tech.ericwathome.core.domain.util.CurrencyUtils
import tech.ericwathome.core.network.converter.serializers.LocalDateSerializer
import java.time.LocalDateTime

@Keep
@Serializable
data class ExchangeRatesDto(
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDateTime?,
    val rates: Map<String, Map<String, Double>>,
) {
    fun toDomain(
        base: String,
        quote: String,
        rate: Double,
        amount: Double,
    ): ExchangeRate {
        return ExchangeRate(
            baseCode = base,
            targetCode = quote,
            conversionRate = with(CurrencyUtils) { rate.roundToDecimalPlaces(2) },
            conversionResult = with(CurrencyUtils) { (rate * amount).roundToDecimalPlaces(2) },
            amount = amount,
        )
    }
}