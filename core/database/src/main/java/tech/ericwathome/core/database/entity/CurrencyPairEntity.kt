package tech.ericwathome.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CurrencyPairEntity(
    @PrimaryKey
    val id: String = "",
    @Embedded(prefix = "base_")
    val baseCurrency: CurrencyDetailsDbModel,
    @Embedded(prefix = "quote_")
    val quoteCurrency: CurrencyDetailsDbModel,
)

data class CurrencyDetailsDbModel(
    val code: String,
    val name: String,
    val symbol: String,
    val flagPng: String,
    val flagSvg: String
)

