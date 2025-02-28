package tech.ericwathome.core.presentation.designsystem.assets

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import tech.ericwathome.core.presentation.designsystem.R

internal val LogoCompactLightMode: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.logo_compact_light_mode)

internal val LogoCompactDarkMode: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.logo_compact_dark_mode)

val LogoCompact: ImageVector
    @Composable
    get() {
        return when (isSystemInDarkTheme()) {
            true -> LogoCompactDarkMode
            false -> LogoCompactLightMode
        }
    }

internal val LogoFullLightMode: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.logo_with_text_light_mode)

internal val LogoFullDarkMode: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.logo_with_text_dark_mode)

val LogoFull: ImageVector
    @Composable
    get() {
        return when (isSystemInDarkTheme()) {
            true -> LogoFullDarkMode
            false -> LogoFullLightMode
        }
    }