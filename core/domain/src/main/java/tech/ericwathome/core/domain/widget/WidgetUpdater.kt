package tech.ericwathome.core.domain.widget

import tech.ericwathome.core.domain.converter.model.ExchangeRate

/**
 * Interface for updating multiple widgets.
 *
 * Implementations of this interface are responsible for updating various widgets with the latest data.
 */
interface WidgetUpdater {
    /**
     * Updates the currency converter widget with the provided exchange rate data.
     *
     * @param exchangeRate The latest exchange rate data to display in the widget.
     */
    suspend fun updateConverterWidget(exchangeRate: ExchangeRate)
}