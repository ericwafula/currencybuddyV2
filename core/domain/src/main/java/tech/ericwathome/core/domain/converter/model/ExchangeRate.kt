package tech.ericwathome.core.domain.converter.model

data class ExchangeRate(
    val baseCode: String,
    val conversionRate: Double,
    val conversionResult: Double,
    val targetCode: String,
)
