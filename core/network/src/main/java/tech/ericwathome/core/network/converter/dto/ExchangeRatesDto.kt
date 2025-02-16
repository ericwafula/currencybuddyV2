package tech.ericwathome.core.network.converter.dto

import kotlinx.serialization.Serializable
import tech.ericwathome.core.network.converter.serializers.LocalDateSerializer
import java.time.LocalDateTime

@Serializable
data class ExchangeRatesDto(
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDateTime?,
    val rates: Map<String, Map<String, Double>>
)