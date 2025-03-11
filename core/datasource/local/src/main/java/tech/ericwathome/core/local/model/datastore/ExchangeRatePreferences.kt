package tech.ericwathome.core.local.model.datastore

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRatePreferences(
    val baseCode: String = "",
    val baseFlag: String = "",
    val targetCode: String,
    val targetFlag: String = "",
    val conversionRate: Double,
    val conversionResult: Double,
    val amount: Double,
)
