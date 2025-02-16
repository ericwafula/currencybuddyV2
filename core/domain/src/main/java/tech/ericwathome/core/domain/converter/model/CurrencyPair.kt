package tech.ericwathome.core.domain.converter.model

data class CurrencyPair(
    val id: String = "",
    val baseCurrency: CurrencyDetails,
    val quoteCurrency: CurrencyDetails,
)
