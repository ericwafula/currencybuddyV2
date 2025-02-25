package tech.ericwathome.core.database.mappers

import tech.ericwathome.core.database.entity.CurrencyMetaDataEntity
import tech.ericwathome.core.domain.converter.model.CurrencyMetaData
import tech.ericwathome.core.domain.converter.model.Flag

fun CurrencyMetaDataEntity.toDomain(): CurrencyMetaData {
    return CurrencyMetaData(
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

fun CurrencyMetaData.toEntity(rate: Double = 0.0): CurrencyMetaDataEntity {
    return CurrencyMetaDataEntity(
        code = code,
        name = name,
        symbol = symbol,
        flagPng = flag.png,
        flagSvg = flag.svg,
        rate = rate,
    )
}