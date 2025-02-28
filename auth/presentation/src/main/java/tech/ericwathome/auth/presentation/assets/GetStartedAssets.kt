@file:Keep

package tech.ericwathome.auth.presentation.assets

import androidx.annotation.Keep
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import tech.ericwathome.auth.presentation.R

internal val GetStartedCurrencyImage: ImageVector
    @Composable
    get() {
        return when (isSystemInDarkTheme()) {
            true -> ImageVector.vectorResource(R.drawable.onboarding_currencies_image_dark_mode)
            false -> ImageVector.vectorResource(R.drawable.onboarding_currencies_image_light_mode)
        }
    }