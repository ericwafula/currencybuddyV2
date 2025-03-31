package tech.ericwathome.core.domain.converter.model

data class ExchangeRate(
    val baseCode: String,
    val baseFlagSvg: String = "",
    val baseFlagPng: String = "",
    val targetCode: String,
    val targetFlagSvg: String = "",
    val targetFlagPng: String = "",
    val amount: Double,
    val conversionRate: Double,
    val conversionResult: Double,
)
