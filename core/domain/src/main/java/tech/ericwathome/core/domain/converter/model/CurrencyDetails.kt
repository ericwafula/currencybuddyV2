package tech.ericwathome.core.domain.converter.model

data class CurrencyDetails(
    val code: String,
    val name: String,
    val symbol: String,
    val flag: Flag,
    val isToggled: Boolean = false
)

data class Flag(
    val png: String,
    val svg: String
)