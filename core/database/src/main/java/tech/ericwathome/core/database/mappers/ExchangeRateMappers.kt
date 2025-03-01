package tech.ericwathome.core.database.mappers

import tech.ericwathome.core.database.entity.ExchangeRateEntity
import tech.ericwathome.core.domain.converter.model.ExchangeRate

fun ExchangeRate.toEntity(): ExchangeRateEntity {
    return ExchangeRateEntity(
        baseCode = baseCode,
        targetCode = targetCode,
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
        isDefault = isDefault,
        amount = amount,
    )
}