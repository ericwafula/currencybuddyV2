package tech.ericwathome.core.network.converter.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import tech.ericwathome.core.network.converter.dto.ExchangeRatesDto

object DynamicRatesSerializer : KSerializer<ExchangeRatesDto> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("ExchangeRatesDto") {
            element<String>("date")
            // Note: "rates" is not declared in the JSON, so we don't add it here.
        }

    override fun serialize(
        encoder: Encoder,
        value: ExchangeRatesDto,
    ) {
        throw SerializationException("Serialization is not implemented")
    }

    override fun deserialize(decoder: Decoder): ExchangeRatesDto {
        val jsonDecoder =
            decoder as? JsonDecoder
                ?: throw SerializationException("Expected JsonDecoder")
        val jsonObject = jsonDecoder.decodeJsonElement().jsonObject

        // Extract the date field.
        val date = jsonObject["date"]?.let { Json.decodeFromJsonElement(LocalDateTimeSerializer, it) }

        // Collect all other keys as the rates.
        val rates =
            jsonObject.filterKeys { it != "date" }.mapValues { (_, value) ->
                Json.decodeFromJsonElement<Map<String, Double>>(value)
            }
        return ExchangeRatesDto(date, rates)
    }
}