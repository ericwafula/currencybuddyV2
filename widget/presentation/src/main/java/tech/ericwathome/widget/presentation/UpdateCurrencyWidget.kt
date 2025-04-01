package tech.ericwathome.widget.presentation

import android.content.Context
import android.net.Uri
import androidx.datastore.preferences.core.MutablePreferences
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import tech.ericwathome.core.domain.converter.model.ExchangeRate

private typealias BaseAndQuoteUris = Pair<Uri?, Uri?>

/**
 * Updates the currency widget with the latest exchange rate and flag images.
 *
 * @param context The context to use for updating the widget.
 * @param exchangeRate The latest exchange rate data to display in the widget.
 */

internal suspend fun updateConverterWidget(
    context: Context,
    exchangeRate: ExchangeRate,
) {
    clearOldFlagImages(context)
    val manager = GlanceAppWidgetManager(context)

    val widgetIds = manager.getGlanceIds(CurrencyWidget::class.java)

    updateConverterWidgetState(
        widgetIds = widgetIds,
        context = context,
        exchangeRate = exchangeRate,
        baseUri = "placeholder",
        quoteUri = "placeholder",
    )

    val (baseUri, quoteUri) = getBaseAndQuoteUris(context, exchangeRate)

    updateConverterWidgetState(
        widgetIds = widgetIds,
        context = context,
        exchangeRate = exchangeRate,
        baseUri = baseUri?.toString() ?: "",
        quoteUri = quoteUri?.toString() ?: "",
    )
}

private suspend fun getBaseAndQuoteUris(
    context: Context,
    exchangeRate: ExchangeRate,
): BaseAndQuoteUris {
    return coroutineScope {
        val baseUriDeferred =
            async {
                loadImageAndSaveToFile(
                    context,
                    exchangeRate.baseFlagSvg,
                    "base_${System.currentTimeMillis()}.svg",
                )?.also {
                    context.grantUriPermissionToHomeActivity(it)
                }
            }
        val quoteUriDeferred =
            async {
                loadImageAndSaveToFile(
                    context,
                    exchangeRate.targetFlagSvg,
                    "quote_${System.currentTimeMillis()}.svg",
                )?.also {
                    context.grantUriPermissionToHomeActivity(it)
                }
            }
        val baseUri = baseUriDeferred.await()
        val quoteUri = quoteUriDeferred.await()
        BaseAndQuoteUris(baseUri, quoteUri)
    }
}

private suspend fun updateConverterWidgetState(
    widgetIds: List<GlanceId>,
    context: Context,
    exchangeRate: ExchangeRate,
    baseUri: String,
    quoteUri: String,
) {
    widgetIds.forEach { glanceId ->
        updateAppWidgetState(context, glanceId) { prefs ->
            prefs.updateTextFields(exchangeRate)
            prefs[CurrencyWidget.baseImageUriKey] = baseUri
            prefs[CurrencyWidget.targetImageUriKey] = quoteUri
        }

        CurrencyWidget().update(context, glanceId)
    }
}

private fun MutablePreferences.updateTextFields(exchangeRate: ExchangeRate) {
    this[CurrencyWidget.baseCodeKey] = exchangeRate.baseCode
    this[CurrencyWidget.targetCodeKey] = exchangeRate.targetCode
    this[CurrencyWidget.rateKey] = exchangeRate.conversionRate.toFloat()
}
