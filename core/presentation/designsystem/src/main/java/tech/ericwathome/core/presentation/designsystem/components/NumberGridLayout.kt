@file:Keep

package tech.ericwathome.core.presentation.designsystem.components

import androidx.annotation.Keep
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tech.ericwathome.core.presentation.designsystem.CurrencybuddyTheme
import tech.ericwathome.core.presentation.designsystem.utils.PreviewLightDarkWithBackground

@Composable
fun NumberGridLayout(
    modifier: Modifier = Modifier,
    onClickNumber: (Char) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3),
        reverseLayout = true,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items((1..9).toList()) { digit ->
            SecondaryButton(
                text = digit.toString(),
                onClick = { digit.toString().firstOrNull()?.let { onClickNumber(it) } },
            )
        }
    }
}

@PreviewLightDarkWithBackground
@Composable
private fun NumberGridPreview() {
    CurrencybuddyTheme {
        Surface {
            NumberGridLayout(onClickNumber = {})
        }
    }
}