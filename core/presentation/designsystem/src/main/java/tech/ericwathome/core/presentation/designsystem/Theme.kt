@file:Keep

package tech.ericwathome.core.presentation.designsystem

import android.app.Activity
import androidx.annotation.Keep
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import tech.ericwathome.core.presentation.designsystem.utils.LocalTextUtils
import tech.ericwathome.core.presentation.designsystem.utils.TextUtils
import tech.ericwathome.core.presentation.designsystem.utils.WithProviders
import tech.ericwathome.core.presentation.designsystem.utils.previewSupported

private val LightColorScheme =
    lightColorScheme(
        primary = md_theme_light_primary,
        onPrimary = md_theme_light_on_primary,
        background = md_theme_light_background,
        onBackground = md_theme_light_on_background,
        surface = md_theme_light_surface,
        onSurface = md_theme_light_on_surface,
        secondary = md_theme_light_secondary,
        onSecondary = md_theme_light_on_secondary,
        error = md_theme_light_error,
        onError = md_theme_light_on_error,
    )

private val DarkColorScheme =
    lightColorScheme(
        primary = md_theme_dark_primary,
        onPrimary = md_theme_dark_on_primary,
        background = md_theme_dark_background,
        onBackground = md_theme_dark_on_background,
        surface = md_theme_dark_surface,
        onSurface = md_theme_dark_on_surface,
        secondary = md_theme_dark_secondary,
        onSecondary = md_theme_dark_on_secondary,
        error = md_theme_dark_error,
        onError = md_theme_dark_on_error,
    )

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CurrencybuddyTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme
    val context = LocalContext.current
    val view = LocalView.current
    val window = (context as? Activity)?.window

    if (!view.isInEditMode) {
        val insetsController = WindowCompat.getInsetsController(window!!, view)
        insetsController.isAppearanceLightStatusBars = true
    }

    SharedTransitionLayout {
        WithProviders(
            LocalTextUtils provides TextUtils(),
        ) {
            MaterialTheme(
                colorScheme = colorScheme,
                typography = Typography.previewSupported,
                content = content,
            )
        }
    }
}