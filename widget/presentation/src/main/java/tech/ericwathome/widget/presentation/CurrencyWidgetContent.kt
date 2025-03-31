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
import androidx.glance.appwidget.ImageProvider
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.components.CircleIconButton
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
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

    GlanceTheme {
        Box(
            modifier =
                GlanceModifier
                    .cornerRadius(12.dp)
                    .background(
                        colorProvider = GlanceTheme.colors.background,
                    ),
        ) {
            Column(
                modifier =
                    GlanceModifier
                        .padding(24.dp),
            ) {
                Box(
                    modifier = GlanceModifier.fillMaxWidth(),
                ) {
                    Row(
                        modifier = GlanceModifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Vertical.CenterVertically,
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Text(
                            text = "Currency Buddy",
                            style =
                                TextStyle(
                                    color = GlanceTheme.colors.onBackground,
                                    fontSize = 24.sp,
                                ),
                        )
                    }
                    Row(
                        modifier = GlanceModifier.fillMaxWidth().height(24.dp),
                        verticalAlignment = Alignment.Vertical.CenterVertically,
                        horizontalAlignment = Alignment.End,
                    ) {
                        CircleIconButton(
                            imageProvider = androidx.glance.ImageProvider(tech.ericwathome.core.presentation.designsystem.R.drawable.ic_right),
                            contentDescription = null,
                            modifier =
                                GlanceModifier
                                    .size(42.dp)
                                    .cornerRadius(100.dp),
                            onClick = actionStartActivity(intent),
                        )
                    }
                }
                Spacer(modifier = GlanceModifier.height(12.dp))
                Box(
                    modifier =
                        GlanceModifier
                            .fillMaxWidth()
                            .background(
                                colorProvider =
                                    ColorProvider(
                                        color =
                                            GlanceTheme
                                                .colors
                                                .onBackground
                                                .getColor(context)
                                                .copy(0.1f),
                                    ),
                            )
                            .height(1.dp),
                ) {}
                Spacer(modifier = GlanceModifier.height(12.dp))
                Box(
                    modifier = GlanceModifier.fillMaxWidth(),
                ) {
                    Row(
                        modifier = GlanceModifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Vertical.CenterVertically,
                        horizontalAlignment = Alignment.Start,
                    ) {
                        if (baseUri != null && baseUri != Uri.EMPTY) {
                            Image(
                                provider = ImageProvider(baseUri),
                                contentDescription = null,
                                modifier =
                                    GlanceModifier
                                        .size(42.dp)
                                        .cornerRadius(100.dp),
                                contentScale = ContentScale.Crop,
                            )
                        } else {
                            Box(
                                modifier =
                                    GlanceModifier
                                        .size(42.dp)
                                        .cornerRadius(100.dp)
                                        .background(
                                            colorProvider =
                                                ColorProvider(
                                                    GlanceTheme.colors.onBackground.getColor(context).copy(0.4f),
                                                ),
                                        ),
                            ) {}
                        }
                        Spacer(modifier = GlanceModifier.width(12.dp))
                        Text(
                            text = baseCode.uppercase(),
                            style =
                                TextStyle(
                                    color = GlanceTheme.colors.onBackground,
                                    fontSize = 24.sp,
                                ),
                        )
                    }
                    Row(
                        modifier = GlanceModifier.fillMaxWidth().height(42.dp),
                        verticalAlignment = Alignment.Vertical.CenterVertically,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "VS",
                            style =
                                TextStyle(
                                    color =
                                        ColorProvider(
                                            GlanceTheme.colors.onBackground.getColor(context).copy(0.5f),
                                        ),
                                    fontSize = 14.sp,
                                ),
                        )
                    }
                    Row(
                        modifier = GlanceModifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Vertical.CenterVertically,
                        horizontalAlignment = Alignment.End,
                    ) {
                        if (quoteUri != null && quoteUri != Uri.EMPTY) {
                            Image(
                                provider = ImageProvider(quoteUri),
                                contentDescription = null,
                                modifier =
                                    GlanceModifier
                                        .size(42.dp)
                                        .cornerRadius(100.dp),
                                contentScale = ContentScale.Crop,
                            )
                        } else {
                            Box(
                                modifier =
                                    GlanceModifier
                                        .size(42.dp)
                                        .cornerRadius(100.dp)
                                        .background(
                                            colorProvider =
                                                ColorProvider(
                                                    GlanceTheme.colors.onBackground.getColor(context).copy(0.4f),
                                                ),
                                        ),
                            ) {}
                        }
                        Spacer(modifier = GlanceModifier.width(12.dp))
                        Text(
                            text = targetCode.uppercase(),
                            style =
                                TextStyle(
                                    color = GlanceTheme.colors.onBackground,
                                    fontSize = 24.sp,
                                ),
                        )
                    }
                }
                Spacer(modifier = GlanceModifier.height(12.dp))
                Box(
                    modifier = GlanceModifier.fillMaxWidth(),
                ) {
                    Row(
                        modifier = GlanceModifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Vertical.CenterVertically,
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Image(
                            provider = androidx.glance.ImageProvider(tech.ericwathome.core.presentation.designsystem.R.drawable.ic_bull),
                            contentDescription = null,
                        )
                    }
                    Box(
                        modifier = GlanceModifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = rate.toString(),
                            style =
                                TextStyle(
                                    color = GlanceTheme.colors.onBackground,
                                    fontSize = 24.sp,
                                ),
                        )
                    }
                    Row(
                        modifier = GlanceModifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Vertical.CenterVertically,
                        horizontalAlignment = Alignment.End,
                    ) {
                        Image(
                            provider = androidx.glance.ImageProvider(tech.ericwathome.core.presentation.designsystem.R.drawable.ic_bear),
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}