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

val UpIconThin: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_up_thin)

val SwapIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_swap)

val FrownIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_frown)

val SearchIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_search)

val BellIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_bell)

val BullIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_bull)

val BearIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_bear)