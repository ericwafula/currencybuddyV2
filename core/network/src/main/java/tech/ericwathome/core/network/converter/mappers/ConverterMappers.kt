@file:Keep

package tech.ericwathome.core.network.converter.mappers

import androidx.annotation.Keep
import tech.ericwathome.core.network.converter.dto.CurrencyMetadataDto
import tech.ericwathome.core.network.converter.dto.FlagDto

fun CurrencyMetadataDto.toDomain() =
    tech.ericwathome.core.domain.converter.model.CurrencyMetadata(
        code = code,
        name = name,
        symbol = symbol,
        flag = flag.toDomain(),
    )

fun FlagDto.toDomain() =
    tech.ericwathome.core.domain.converter.model.Flag(
        png = png,
        svg = svg,
    )