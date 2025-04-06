package tech.ericwathome.widget.presentation

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.appwidget.ImageProvider
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.layout.wrapContentHeight
import androidx.glance.layout.wrapContentWidth
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

@Composable
fun CurrencyWidgetContent(isMiui: Boolean) {
    val prefs = currentState<Preferences>()
    val context = LocalContext.current
    val intent =
        Intent().apply {
            data = "currencybuddy://converter".toUri()
            component = ComponentName("tech.ericwathome.currencybuddy", "tech.ericwathome.currencybuddy.MainActivity")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
    val baseCode = prefs[CurrencyWidget.baseCodeKey] ?: "EUR"
    val baseUri = prefs[CurrencyWidget.baseImageUriKey]?.takeIf { it != "placeholder" }?.toUri()
    val targetCode = prefs[CurrencyWidget.targetCodeKey] ?: "USD"
    val quoteUri = prefs[CurrencyWidget.targetImageUriKey]?.takeIf { it != "placeholder" }?.toUri()
    val rate = prefs[CurrencyWidget.rateKey] ?: 0.0f

    Box(
        modifier =
            GlanceModifier
                .cornerRadius(12.dp)
                .wrapContentWidth()
                .wrapContentHeight()
                .background(colorProvider = GlanceTheme.colors.surface)
                .clickable(actionStartActivity(intent)),
    ) {
        Row(
            modifier =
                GlanceModifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .padding(16.dp),
            verticalAlignment = Alignment.Vertical.CenterVertically,
        ) {
            Column {
                Text(
                    text = "From",
                    style =
                        TextStyle(
                            color = ColorProvider(GlanceTheme.colors.onPrimary.getColor(context).copy(0.5f)),
                            fontSize = 10.sp,
                        ),
                )
                Spacer(modifier = GlanceModifier.height(10.dp))
                if (baseUri != null && baseUri != Uri.EMPTY) {
                    Image(
                        provider = ImageProvider(baseUri),
                        contentDescription = null,
                        modifier =
                            GlanceModifier
                                .size(32.dp)
                                .cornerRadius(100.dp),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Box(
                        modifier =
                            GlanceModifier
                                .size(32.dp)
                                .cornerRadius(100.dp)
                                .background(
                                    colorProvider =
                                        ColorProvider(
                                            GlanceTheme.colors.onBackground.getColor(context).copy(0.4f),
                                        ),
                                ),
                    ) {}
                }
                Spacer(modifier = GlanceModifier.height(8.dp))
                Text(
                    text = baseCode.uppercase(),
                    style =
                        TextStyle(
                            color = GlanceTheme.colors.onBackground,
                            fontSize = 14.sp,
                        ),
                )
            }
            Spacer(modifier = GlanceModifier.width(14.dp))
            Text(
                modifier = GlanceModifier,
                text = "=",
                style =
                    TextStyle(
                        color = GlanceTheme.colors.onBackground,
                        fontSize = 20.sp,
                    ),
            )
            Spacer(modifier = GlanceModifier.width(14.dp))
            Column {
                Text(
                    text = "To",
                    style =
                        TextStyle(
                            color = ColorProvider(GlanceTheme.colors.onPrimary.getColor(context).copy(0.5f)),
                            fontSize = 10.sp,
                        ),
                )
                Spacer(modifier = GlanceModifier.height(10.dp))
                if (quoteUri != null && quoteUri != Uri.EMPTY) {
                    Image(
                        provider = ImageProvider(quoteUri),
                        contentDescription = null,
                        modifier =
                            GlanceModifier
                                .size(32.dp)
                                .cornerRadius(100.dp),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Box(
                        modifier =
                            GlanceModifier
                                .size(32.dp)
                                .cornerRadius(100.dp)
                                .background(
                                    colorProvider =
                                        ColorProvider(
                                            GlanceTheme.colors.onBackground.getColor(context).copy(0.4f),
                                        ),
                                ),
                    ) {}
                }
                Spacer(modifier = GlanceModifier.height(8.dp))
                Text(
                    modifier = GlanceModifier,
                    text = "$rate ${targetCode.uppercase()}",
                    style =
                        TextStyle(
                            color = GlanceTheme.colors.primary,
                            fontSize = 14.sp,
                        ),
                )
            }
        }
    }
}