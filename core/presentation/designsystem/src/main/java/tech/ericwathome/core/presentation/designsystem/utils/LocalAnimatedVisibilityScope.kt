@file:Keep

package tech.ericwathome.core.presentation.designsystem.utils

import androidx.annotation.Keep
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf

val LocalAnimatedVisibilityScope =
    compositionLocalOf<AnimatedVisibilityScope> {
        error("Must be provided first")
    }

@Composable
fun AnimatedVisibilityScope.ProvideAnimatedVisibilityScope(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalAnimatedVisibilityScope provides this,
        content = content,
    )
}

@Composable
fun WithAnimatedVisibilityScope(block: @Composable AnimatedVisibilityScope.() -> Unit) {
    with(LocalAnimatedVisibilityScope.current) {
        block()
    }
}