package tech.ericwathome.converter.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tech.ericwathome.core.presentation.designsystem.CurrencybuddyTheme
import tech.ericwathome.core.presentation.designsystem.components.CurrencyBuddyActionRowLayout
import tech.ericwathome.core.presentation.designsystem.components.CurrencyBuddyControlColumnLayout
import tech.ericwathome.core.presentation.designsystem.components.CurrencyBuddyNumberGridLayout
import tech.ericwathome.core.presentation.designsystem.utils.PreviewLightDarkWithBackground

@Composable
fun ConverterKeyboard(
    modifier: Modifier = Modifier,
    onClickInputKey: (Char) -> Unit,
    onClickConvert: () -> Unit,
    onClickDelete: () -> Unit,
    onClickClear: () -> Unit,
    padding: PaddingValues = PaddingValues(16.dp),
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(padding),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CurrencyBuddyNumberGridLayout(
                modifier = Modifier.weight(1f),
                onClickNumber = { onClickInputKey(it) },
            )
            CurrencyBuddyControlColumnLayout(
                modifier = Modifier,
                onClickAC = onClickClear,
                onClickDEL = onClickDelete,
            )
        }
        CurrencyBuddyActionRowLayout(
            modifier = Modifier.fillMaxWidth(),
            onClickInputKey = onClickInputKey,
            ctaText = stringResource(tech.ericwathome.core.presentation.ui.R.string.convert),
            onClickCta = onClickConvert,
        )
    }
}

@PreviewLightDarkWithBackground
@Composable
private fun ConverterKeyboardPreview() {
    CurrencybuddyTheme {
        Surface {
            ConverterKeyboard(
                modifier = Modifier,
                onClickConvert = {},
                onClickDelete = {},
                onClickClear = {},
                onClickInputKey = { },
            )
        }
    }
}