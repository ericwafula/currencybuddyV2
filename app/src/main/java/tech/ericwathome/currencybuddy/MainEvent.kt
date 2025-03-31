package tech.ericwathome.currencybuddy

import tech.ericwathome.core.domain.converter.model.ExchangeRate

sealed interface MainEvent {
    data class UpdateCurrencyWidget(val exchangeRate: ExchangeRate) : MainEvent
}