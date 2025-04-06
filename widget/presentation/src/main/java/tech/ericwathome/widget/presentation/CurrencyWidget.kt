package tech.ericwathome.widget.presentation

import android.content.Context
import android.os.Build
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import org.koin.core.component.KoinComponent
import tech.ericwathome.widget.presentation.theme.CurrencyBuddyWidgetColorScheme

class CurrencyWidget : GlanceAppWidget(), KoinComponent {
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId,
    ) {
        provideContent {
            GlanceTheme(
                colors = CurrencyBuddyWidgetColorScheme.colors,
            ) {
                CurrencyWidgetContent(
                    isMiui = isMiui(),
                )
            }
        }
    }

    private fun isMiui(): Boolean {
        return Build.MANUFACTURER.equals("xiaomi", ignoreCase = true) ||
            Build.BRAND.equals("xiaomi", ignoreCase = true)
    }

    companion object {
        val baseCodeKey = stringPreferencesKey("baseCode")
        val baseImageUriKey = stringPreferencesKey("baseImageUri")
        val targetCodeKey = stringPreferencesKey("targetCode")
        val targetImageUriKey = stringPreferencesKey("targetImageUri")
        val rateKey = floatPreferencesKey("rate")
        val lastUpdatedKey = longPreferencesKey("lastUpdated")
    }
}
