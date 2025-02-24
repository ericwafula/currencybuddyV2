package tech.ericwathome.core.database.mappers

import tech.ericwathome.core.database.entity.CurrencyDetailsEntity
import tech.ericwathome.core.domain.converter.model.CurrencyDetails
import tech.ericwathome.core.domain.converter.model.Flag

fun CurrencyDetailsEntity.toDomain(): CurrencyDetails {
    return CurrencyDetails(
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

fun CurrencyDetails.toEntity(rate: Double = 0.0): CurrencyDetailsEntity {
    return CurrencyDetailsEntity(
        code = code,
        name = name,
        symbol = symbol,
        flagPng = flag.png,
        flagSvg = flag.svg,
        rate = rate,
    )
}