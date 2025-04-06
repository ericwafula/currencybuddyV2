@file:Keep

package tech.ericwathome.core.presentation.designsystem.utils

import androidx.annotation.Keep
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize

@Composable
fun Modifier.shimmerEffect(): Modifier {
    val shimmerColor = MaterialTheme.colorScheme.primary.copy(0.1f)

    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition(label = "")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec =
            infiniteRepeatable(
                animation = tween(1000),
            ),
        label = "",
    )

    return background(
        brush =
            Brush.linearGradient(
                colors =
                    listOf(
                        shimmerColor.copy(0.1f),
                        shimmerColor.copy(0.7f),
                        shimmerColor.copy(0.1f),
                    ),
                start = Offset(startOffsetX, 0f),
                end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat()),
            ),
    ).onGloballyPositioned {
        size = it.size
    }
}