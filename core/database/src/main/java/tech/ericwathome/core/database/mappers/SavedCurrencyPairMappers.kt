package tech.ericwathome.core.database.mappers

import tech.ericwathome.core.database.entity.CurrencyDetailsDbModel
import tech.ericwathome.core.database.entity.CurrencyPairEntity
import tech.ericwathome.core.domain.converter.model.CurrencyDetails
import tech.ericwathome.core.domain.converter.model.Flag
import tech.ericwathome.core.domain.converter.model.CurrencyPair

fun CurrencyPairEntity.toCurrencyPair(): CurrencyPair {
    return CurrencyPair(
        id = id,
        baseCurrency = baseCurrency.toCurrencyDetails(),
        quoteCurrency = quoteCurrency.toCurrencyDetails()
    )
}

fun CurrencyDetailsDbModel.toCurrencyDetails(): CurrencyDetails {
    return CurrencyDetails(
        code = code,
        name = name,
        symbol = symbol,
        flag = Flag(
            png = flagPng,
            svg = flagSvg
        )
    )
}

fun CurrencyPair.toCurrencyPairEntity(): CurrencyPairEntity {
    return CurrencyPairEntity(
        id = id,
        baseCurrency = baseCurrency.toCurrencyDetailsDbModel(),
        quoteCurrency = quoteCurrency.toCurrencyDetailsDbModel()
    )
}

fun CurrencyDetails.toCurrencyDetailsDbModel(): CurrencyDetailsDbModel {
    return CurrencyDetailsDbModel(
        code = code,
        name = name,
        symbol = symbol,
        flagPng = flag.png,
        flagSvg = flag.svg
    )
}