package tech.ericwathome.converter.presentation

import tech.ericwathome.core.domain.converter.model.CurrencyMetadata
import tech.ericwathome.core.presentation.ui.UiText

data class ConverterState(
    val amount: String = "1",
    val result: String = "0",
    val baseCurrencyCode: String = "EUR",
    val currentlySelectedBaseCurrencyMetadata: CurrencyMetadata? = null,
    val quoteCurrencyCode: String = "USD",
    val currentlySelectedQuoteCurrencyMetadata: CurrencyMetadata? = null,
    val baseFlagUrl: String = "",
    val quoteFlagUrl: String = "",
    val converting: Boolean = false,
    val isError: Boolean = false,
    val isSyncing: Boolean = false,
    val errorMessage: UiText? = null,
    val showCurrencyPickerBottomSheet: Boolean = false,
    val currencyMetadataList: List<CurrencyMetadata> = emptyList(),
    val currencyMetadataMap: Map<String, CurrencyMetadata> = emptyMap(),
    val searchQuery: String = "",
    val isBaseCurrencyToggled: Boolean = true,
    val canContinue: Boolean = false,
)
