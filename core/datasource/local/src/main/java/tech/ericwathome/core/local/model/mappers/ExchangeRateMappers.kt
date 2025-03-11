package tech.ericwathome.core.local.model.mappers

import tech.ericwathome.core.domain.converter.model.ExchangeRate
import tech.ericwathome.core.local.model.datastore.ExchangeRatePreferences
import tech.ericwathome.core.local.model.entity.ExchangeRateEntity

fun ExchangeRate.toEntity(): ExchangeRateEntity {
    return ExchangeRateEntity(
        baseCode = baseCode,
        targetCode = targetCode,
        conversionRate = conversionRate,
        conversionResult = conversionResult,
        amount = amount,
    )
}

fun ExchangeRate.toPreferences(): ExchangeRatePreferences {
    return ExchangeRatePreferences(
        baseCode = baseCode,
        baseFlag = baseFlag,
        targetCode = targetCode,
        targetFlag = targetFlag,
        conversionRate = conversionRate,
        conversionResult = conversionResult,
        amount = amount,
    )
}

fun ExchangeRateEntity.toDomain(): ExchangeRate {
    return ExchangeRate(
        baseCode = baseCode,
        targetCode = targetCode,
        conversionRate = conversionRate,
        conversionResult = conversionResult,
        amount = amount,
    )
}

fun ExchangeRatePreferences.toDomain(): ExchangeRate {
    return ExchangeRate(
        baseCode = baseCode,
        baseFlag = baseFlag,
        targetCode = targetCode,
        targetFlag = targetFlag,
        amount = amount,
        conversionRate = conversionRate,
        conversionResult = conversionResult,
    )
}