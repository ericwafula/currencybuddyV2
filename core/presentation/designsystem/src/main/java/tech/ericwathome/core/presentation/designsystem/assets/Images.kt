@file:Keep

package tech.ericwathome.core.presentation.designsystem.assets

import androidx.annotation.Keep
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import tech.ericwathome.core.presentation.designsystem.R

internal val EmptyImageLight: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.empty_image_light)

internal val EmptyImageDark: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.empty_image_dark)

val EmptyImage: ImageVector
    @Composable
    get() = if (isSystemInDarkTheme()) EmptyImageDark else EmptyImageLight