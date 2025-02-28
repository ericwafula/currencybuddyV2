package tech.ericwathome.core.presentation.designsystem.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

class TextUtils {
    val TextUnit.fixedSize
        @Composable
        get() = (this.value / LocalDensity.current.fontScale).sp
}

val LocalTextUtils = compositionLocalOf { TextUtils() }