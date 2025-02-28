@file:[Keep OptIn(ExperimentalSharedTransitionApi::class)]

package tech.ericwathome.core.presentation.designsystem.utils

import androidx.annotation.Keep
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.compositionLocalOf

val LocalSharedTransitionScope =
    compositionLocalOf<SharedTransitionScope> {
        error("Must be provided first")
    }

@Composable
fun SharedTransitionScope.WithProviders(
    vararg providers: ProvidedValue<*>,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalSharedTransitionScope provides this,
        *providers,
        content = content,
    )
}

@Composable
fun WithSharedTransitionScope(block: @Composable SharedTransitionScope.() -> Unit) {
    with(LocalSharedTransitionScope.current) {
        block()
    }
}