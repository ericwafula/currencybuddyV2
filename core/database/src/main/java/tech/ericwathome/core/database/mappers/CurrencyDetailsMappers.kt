package tech.ericwathome.core.database.mappers

import tech.ericwathome.core.database.entity.CurrencyMetadataEntity
import tech.ericwathome.core.domain.converter.model.CurrencyMetadata
import tech.ericwathome.core.domain.converter.model.Flag

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