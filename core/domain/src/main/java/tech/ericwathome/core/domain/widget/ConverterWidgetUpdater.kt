package tech.ericwathome.core.domain.widget

import tech.ericwathome.core.domain.converter.model.ExchangeRate

/**
 * Interface for updating the currency converter widget.
 *
 * Implementations of this interface are responsible for updating the widget with the latest exchange rate data.
 */
interface ConverterWidgetUpdater {
    suspend fun update(exchangeRate: ExchangeRate)
}