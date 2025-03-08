package tech.ericwathome.core.domain.converter.model

data class CurrencyMetadata(
    val code: String,
    val name: String,
    val symbol: String,
    val flag: Flag,
    val isSelected: Boolean = false,
)

data class Flag(
    val png: String,
    val svg: String,
)