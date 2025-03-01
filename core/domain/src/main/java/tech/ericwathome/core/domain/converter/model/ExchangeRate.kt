package tech.ericwathome.core.domain.converter.model

data class ExchangeRate(
    val baseCode: String,
    val baseFlag: String = "",
    val targetCode: String,
    val targetFlag: String = "",
    val amount: Double,
    val conversionRate: Double,
    val conversionResult: Double,
    val isDefault: Boolean = false,
)
