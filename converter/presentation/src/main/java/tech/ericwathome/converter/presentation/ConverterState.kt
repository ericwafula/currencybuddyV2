package tech.ericwathome.converter.presentation

data class ConverterState(
    val input: String = "",
    val result: String = "",
    val baseCurrencyCode: String = "EUR",
    val quoteCurrencyCode: String = "USD",
    val baseFlagUrl: String = "",
    val quoteFlagUrl: String = "",
    val converting: Boolean = false,
)
