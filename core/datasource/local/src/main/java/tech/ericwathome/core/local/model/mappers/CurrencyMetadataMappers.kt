package tech.ericwathome.core.local.model.mappers

import tech.ericwathome.core.domain.converter.model.CurrencyMetadata
import tech.ericwathome.core.domain.converter.model.Flag
import tech.ericwathome.core.local.model.entity.CurrencyMetadataEntity

fun CurrencyMetadataEntity.toDomain(): CurrencyMetadata {
    return CurrencyMetadata(
        code = code,
        name = name,
        symbol = symbol,
        flag =
            Flag(
                png = flagPng,
                svg = flagSvg,
            ),
    )
}

fun CurrencyMetadata.toEntity(rate: Double = 0.0): CurrencyMetadataEntity {
    return CurrencyMetadataEntity(
        code = code,
        name = name,
        symbol = symbol,
        flagPng = flag.png,
        flagSvg = flag.svg,
        rate = rate,
    )
}