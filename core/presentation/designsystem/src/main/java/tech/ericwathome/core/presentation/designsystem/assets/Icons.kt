@file:Keep

package tech.ericwathome.core.presentation.designsystem.assets

import androidx.annotation.Keep
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import tech.ericwathome.core.presentation.designsystem.R

val DeleteIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_delete)

val BackIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_back)

val DownIconThin: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_down_thin)

val SwapIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_swap)

val FrownIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_frown)