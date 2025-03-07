package tech.ericwathome.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import tech.ericwathome.core.presentation.designsystem.CurrencybuddyTheme

@Composable
fun CurrencyBuddyRadioIcon(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
) {
    Box(
        modifier =
            modifier
                .size(20.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = CircleShape,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier =
                modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(
                        if (selected) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            Color.Transparent
                        },
                    ),
        )
    }
}

@PreviewLightDark
@Composable
private fun CurrencyBuddyRadioIconPreview() {
    CurrencybuddyTheme {
        Surface {
            Box(modifier = Modifier.padding(16.dp)) {
                CurrencyBuddyRadioIcon(
                    modifier = Modifier,
                    selected = true,
                )
            }
        }
    }
}