package tech.ericwathome.core.local.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CurrencyMetadataEntity(
    @PrimaryKey
    val code: String,
    val name: String,
    val symbol: String,
    val flagSvg: String,
    val flagPng: String,
    val rate: Double,
)