@file:Keep

package tech.ericwathome.core.presentation.designsystem.components

import androidx.annotation.Keep
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tech.ericwathome.core.presentation.designsystem.CurrencybuddyTheme
import tech.ericwathome.core.presentation.designsystem.utils.PreviewLightDarkWithBackground

@Composable
fun ControlColumnLayout(
    modifier: Modifier = Modifier,
    onClickAC: () -> Unit,
    onClickDEL: () -> Unit,
) {
    Column(
        modifier = modifier.width(80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SecondaryButtonOutlined(
            modifier = Modifier.fillMaxWidth(),
            text = "AC",
            onClick = onClickAC,
        )
        SecondaryButtonOutlined(
            modifier = Modifier.fillMaxWidth().height(136.dp),
            text = "DEL",
            onClick = onClickDEL,
        )
    }
}

@PreviewLightDarkWithBackground
@Composable
private fun ControlColumnLayoutPreview() {
    CurrencybuddyTheme {
        Surface {
            ControlColumnLayout(
                modifier = Modifier,
                onClickAC = {},
                onClickDEL = {},
            )
        }
    }
}