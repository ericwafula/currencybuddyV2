package tech.ericwathome.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExchangeRateEntity(
    @PrimaryKey
    val baseCode: String = "",
    val baseFlag: String = "",
    val targetCode: String,
    val targetFlag: String = "",
    val conversionRate: Double,
    val conversionResult: Double,
    val isDefault: Boolean = false,
    val amount: Double,
)
