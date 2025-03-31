package tech.ericwathome.currencybuddy.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.flow.firstOrNull
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tech.ericwathome.core.data.util.launchCoroutineScope
import tech.ericwathome.core.domain.converter.ConverterRepository
import tech.ericwathome.widget.presentation.updateCurrencyWidget

class UpdateCurrencyWidgetReceiver : BroadcastReceiver(), KoinComponent {
    private val converterRepository by inject<ConverterRepository>()

    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {
        launchCoroutineScope {
            val exchangeRate = converterRepository.exchangeRateObservable.firstOrNull()

            if (context != null && exchangeRate != null) {
                updateCurrencyWidget(context, exchangeRate)
            }
        }
    }
}