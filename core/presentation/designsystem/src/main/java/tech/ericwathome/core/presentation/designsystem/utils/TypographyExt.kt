package tech.ericwathome.core.presentation.designsystem.utils

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily

private fun TextStyle.withPreviewFallback(isPreview: Boolean): TextStyle = if (isPreview) copy(fontFamily = FontFamily.Default) else this

internal val Typography.previewSupported: Typography
    @Composable
    get() {
        val isPreview = LocalInspectionMode.current
        return this.copy(
            displayLarge = displayLarge.withPreviewFallback(isPreview),
            displayMedium = displayMedium.withPreviewFallback(isPreview),
            displaySmall = displaySmall.withPreviewFallback(isPreview),
            bodyLarge = bodyLarge.withPreviewFallback(isPreview),
            bodyMedium = bodyMedium.withPreviewFallback(isPreview),
            bodySmall = bodySmall.withPreviewFallback(isPreview),
            titleLarge = titleLarge.withPreviewFallback(isPreview),
            titleMedium = titleMedium.withPreviewFallback(isPreview),
            titleSmall = titleSmall.withPreviewFallback(isPreview),
            labelLarge = labelLarge.withPreviewFallback(isPreview),
            labelMedium = labelMedium.withPreviewFallback(isPreview),
            labelSmall = labelSmall.withPreviewFallback(isPreview),
        )
    }