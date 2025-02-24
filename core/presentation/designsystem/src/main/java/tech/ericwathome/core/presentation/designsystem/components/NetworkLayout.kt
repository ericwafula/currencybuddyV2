package tech.ericwathome.core.presentation.designsystem.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun NetworkLayout(
    modifier: Modifier = Modifier,
    onRetry: () -> Unit,
    content: @Composable () -> Unit
) {

}

@Preview
@Composable
private fun NetworkLayoutPreview() {
    NetworkLayout(
        onRetry = {},
        content = {}
    )
}