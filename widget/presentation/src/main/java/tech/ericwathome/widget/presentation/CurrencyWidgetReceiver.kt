package tech.ericwathome.widget.presentation

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tech.ericwathome.core.domain.converter.ConverterRepository

class CurrencyWidgetReceiver : GlanceAppWidgetReceiver(), KoinComponent {
    private val converterRepository by inject<ConverterRepository>()

    override val glanceAppWidget: GlanceAppWidget = CurrencyWidget()

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        CoroutineScope(Dispatchers.IO).launch {
            val exchangeRate = converterRepository.exchangeRateObservable.firstOrNull()
            exchangeRate?.let {
                updateCurrencyWidget(context, it)
            }
        }
    }
}