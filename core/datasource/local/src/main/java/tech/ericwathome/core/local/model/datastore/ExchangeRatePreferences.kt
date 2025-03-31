package tech.ericwathome.core.local.model.datastore

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRatePreferences(
    val baseCode: String = "",
    val baseFlagSvg: String = "",
    val baseFlagPng: String = "",
    val targetCode: String,
    val targetFlagSvg: String = "",
    val targetFlagPng: String = "",
    val conversionRate: Double,
    val conversionResult: Double,
    val amount: Double,
)
