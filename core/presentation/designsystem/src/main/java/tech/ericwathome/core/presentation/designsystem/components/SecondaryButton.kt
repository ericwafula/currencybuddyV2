package tech.ericwathome.core.presentation.designsystem.components

import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tech.ericwathome.core.presentation.designsystem.CurrencybuddyTheme
import tech.ericwathome.core.presentation.designsystem.utils.LocalTextUtils
import tech.ericwathome.core.presentation.designsystem.utils.PreviewLightDarkWithBackground

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
) {
    val textUtils = LocalTextUtils.current

    TextButton(
        modifier =
            modifier
                .sizeIn(minHeight = 56.dp, minWidth = 56.dp),
        onClick = onClick,
    ) {
        Text(
            text = text,
            style =
                MaterialTheme.typography.bodyLarge.copy(
                    fontSize = with(textUtils) { MaterialTheme.typography.bodyLarge.fontSize.fixedSize },
                ),
        )
    }
}

@PreviewLightDarkWithBackground
@Composable
private fun SecondaryButtonPreview() {
    CurrencybuddyTheme {
        Surface {
            SecondaryButton(
                modifier = Modifier,
                onClick = { },
                text = "7",
            )
        }
    }
}