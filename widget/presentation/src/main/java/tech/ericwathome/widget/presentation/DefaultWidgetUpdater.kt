package tech.ericwathome.widget.presentation

import tech.ericwathome.core.domain.converter.model.ExchangeRate
import tech.ericwathome.core.domain.widget.ConverterWidgetUpdater
import tech.ericwathome.core.domain.widget.WidgetUpdater

/**
 * Default implementation of the [WidgetUpdater] interface.
 *
 * This class is responsible for updating different widgets with the latest data
 * by delegating the update to the appropriate [ConverterWidgetUpdater].
 *
 * @property converterUpdater The [ConverterWidgetUpdater] used to update the currency converter widget.
 */
internal class DefaultWidgetUpdater(
    private val converterUpdater: ConverterWidgetUpdater,
) : WidgetUpdater {
    /**
     * Updates the currency converter widget with the provided exchange rate data.
     *
     * @param exchangeRate The latest exchange rate data to display in the widget.
     */
    override suspend fun updateConverterWidget(exchangeRate: ExchangeRate) {
        converterUpdater.update(exchangeRate)
    }
}