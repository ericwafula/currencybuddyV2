package tech.ericwathome.converter.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import tech.ericwathome.converter.presentation.R
import tech.ericwathome.core.presentation.designsystem.CurrencybuddyTheme
import tech.ericwathome.core.presentation.designsystem.assets.SearchIcon
import tech.ericwathome.core.presentation.designsystem.components.CurrencyBuddyTextField

@Composable
fun CurrencyBuddySearchTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    hint: String = "",
) {
    CurrencyBuddyTextField(
        modifier = modifier,
        state = state,
        hint = hint,
        leadingIcon = SearchIcon,
    )
}

@Composable
fun CurrencyBuddySearchTextField(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onEnterSearchQuery: (String) -> Unit,
    hint: String = "",
) {
    CurrencyBuddyTextField(
        modifier = modifier,
        value = searchQuery,
        onValueChange = onEnterSearchQuery,
        hint = hint,
        leadingIcon = SearchIcon,
    )
}

@PreviewLightDark
@Composable
private fun CurrencyBuddySearchTextFieldPreview() {
    CurrencybuddyTheme {
        Surface {
            Box(
                modifier = Modifier.padding(16.dp),
            ) {
                CurrencyBuddySearchTextField(
                    state = TextFieldState(),
                    modifier = Modifier.fillMaxWidth(),
                    hint = stringResource(R.string.search_for_currency),
                )
            }
        }
    }
}