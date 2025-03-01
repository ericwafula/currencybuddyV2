package tech.ericwathome.converter.presentation

import tech.ericwathome.core.presentation.ui.UiText

data class ConverterState(
    val amount: String = "",
    val result: String = "",
    val baseCurrencyCode: String = "EUR",
    val quoteCurrencyCode: String = "USD",
    val baseFlagUrl: String = "",
    val quoteFlagUrl: String = "",
    val converting: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: UiText? = null,
)
