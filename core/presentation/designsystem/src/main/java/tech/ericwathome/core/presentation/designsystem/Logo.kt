package tech.ericwathome.core.presentation.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

val LogoCompactLightMode: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.logo_compact_light_mode)

val LogoCompactDarkMode: ImageVector
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