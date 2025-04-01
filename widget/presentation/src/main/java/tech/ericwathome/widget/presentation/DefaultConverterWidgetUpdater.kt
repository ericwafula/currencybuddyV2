package tech.ericwathome.widget.presentation

import android.content.Context
import tech.ericwathome.core.domain.converter.model.ExchangeRate
import tech.ericwathome.core.domain.widget.ConverterWidgetUpdater

/**
 * Default implementation of the [ConverterWidgetUpdater] interface.
 *
 * This class is responsible for updating the currency converter widget with the latest exchange rate data.
 *
 * @property context The context to use for updating the widget.
 */
internal class DefaultConverterWidgetUpdater(
    private val context: Context,
) : ConverterWidgetUpdater {
    override suspend fun update(exchangeRate: ExchangeRate) {
        updateConverterWidget(context, exchangeRate)
    }
}