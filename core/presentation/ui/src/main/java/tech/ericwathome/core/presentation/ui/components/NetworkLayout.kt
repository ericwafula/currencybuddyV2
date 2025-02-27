package tech.ericwathome.core.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tech.ericwathome.core.presentation.designsystem.CurrencybuddyTheme
import tech.ericwathome.core.presentation.designsystem.components.CurrencyBuddyTinyPopup
import tech.ericwathome.core.presentation.designsystem.utils.PreviewLightDarkWithBackground
import tech.ericwathome.core.presentation.ui.R

@Composable
fun NetworkLayout(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    showNetworkPopup: Boolean,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
    ) {
        Box {
            content()
            if (showNetworkPopup) {
                Box(
                    modifier =
                        Modifier
                            .padding(16.dp)
                            .align(Alignment.BottomCenter),
                ) {
                    CurrencyBuddyTinyPopup(
                        modifier = Modifier.fillMaxWidth(),
                        onAccept = onDismiss,
                        onDecline = { },
                        isVisible = true,
                        text = stringResource(R.string.network_unavailable),
                        dismissText = "",
                        acceptText = stringResource(R.string.dismiss),
                    )
                }
            }
        }
    }
}

@PreviewLightDarkWithBackground
@Composable
private fun NetworkLayoutPreview() {
    CurrencybuddyTheme {
        NetworkLayout(
            onDismiss = {},
            content = {},
            showNetworkPopup = true,
        )
    }
}