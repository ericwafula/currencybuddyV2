package tech.ericwathome.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CurrencyDetailsEntity(
    @PrimaryKey
    val code: String,
    val name: String,
    val symbol: String,
    val flagSvg: String,
    val flagPng: String,
    val rate: Double,
)