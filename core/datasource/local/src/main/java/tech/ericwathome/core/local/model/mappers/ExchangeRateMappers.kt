package tech.ericwathome.core.local.model.mappers

import tech.ericwathome.core.domain.converter.model.ExchangeRate
import tech.ericwathome.core.local.model.entity.ExchangeRateEntity

fun ExchangeRate.toEntity(): ExchangeRateEntity {
    return ExchangeRateEntity(
        baseCode = baseCode,
        targetCode = targetCode,
        conversionRate = conversionRate,
        conversionResult = conversionResult,
        amount = amount,
        isDefault = isDefault,
    )
}

fun ExchangeRateEntity.toDomain(): ExchangeRate {
    return ExchangeRate(
        baseCode = baseCode,
        targetCode = targetCode,
        conversionRate = conversionRate,
        conversionResult = conversionResult,
        isDefault = isDefault,
        amount = amount,
    )
}