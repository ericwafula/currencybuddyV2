@file:Keep

package tech.ericwathome.core.presentation.designsystem.components

import androidx.annotation.Keep
import androidx.compose.animation.AnimatedVisibility
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
import tech.ericwathome.core.presentation.designsystem.R
import tech.ericwathome.core.presentation.designsystem.utils.PreviewLightDarkWithBackground

@Composable
fun CurrencyBuddyNetworkLayout(
    modifier: Modifier = Modifier,
    onClickConnect: () -> Unit,
    showNetworkPopup: Boolean,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
    ) {
        Box {
            content()
            AnimatedVisibility(
                modifier =
                    Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomCenter),
                visible = showNetworkPopup,
            ) {
                CurrencyBuddyTinyPopup(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    onAccept = onClickConnect,
                    onDecline = { },
                    isVisible = true,
                    text = stringResource(R.string.network_unavailable),
                    dismissText = "",
                    acceptText = stringResource(R.string.connect),
                )
            }
        }
    }
}

@PreviewLightDarkWithBackground
@Composable
private fun NetworkLayoutPreview() {
    CurrencybuddyTheme {
        CurrencyBuddyNetworkLayout(
            onClickConnect = {},
            content = {},
            showNetworkPopup = true,
        )
    }
}