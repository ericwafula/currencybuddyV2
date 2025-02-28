package tech.ericwathome.core.network.converter.dto

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class CurrencyMetadataDto(
    val code: String,
    val name: String,
    val symbol: String,
    val flag: FlagDto,
)

@Keep
@Serializable
data class FlagDto(
    val png: String,
    val svg: String,
)