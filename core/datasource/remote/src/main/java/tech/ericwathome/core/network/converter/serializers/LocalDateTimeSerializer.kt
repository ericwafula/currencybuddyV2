package tech.ericwathome.core.network.converter.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import tech.ericwathome.core.domain.util.DateUtils
import tech.ericwathome.core.domain.util.tryOrNull
import java.time.LocalDateTime

object LocalDateTimeSerializer : KSerializer<LocalDateTime?> {
    override val descriptor =
        PrimitiveSerialDescriptor(this::class.java.simpleName, PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: LocalDateTime?,
    ) {
        encoder.encodeString(with(DateUtils) { value?.formated() ?: "" })
    }

    override fun deserialize(decoder: Decoder): LocalDateTime? {
        return with(DateUtils) { tryOrNull { decoder.decodeString().toLocalDate() } }
    }
}