@file:OptIn(ExperimentalMaterial3Api::class)

package tech.ericwathome.converter.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tech.ericwathome.converter.presentation.R
import tech.ericwathome.core.domain.converter.model.CurrencyMetadata
import tech.ericwathome.core.domain.converter.model.Flag
import tech.ericwathome.core.presentation.designsystem.CurrencybuddyTheme
import tech.ericwathome.core.presentation.designsystem.assets.EmptyImage
import tech.ericwathome.core.presentation.designsystem.components.CurrencyBuddyPrimaryButton
import tech.ericwathome.core.presentation.designsystem.components.CurrencyBuddySecondaryButtonOutlined
import tech.ericwathome.core.presentation.designsystem.utils.PreviewLightDarkWithBackground

@Composable
fun SelectCurrencyBottomSheet(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onSelectCurrency: (index: Int) -> Unit,
    onClickContinue: () -> Unit,
    onClickRetry: () -> Unit,
    searchQuery: String,
    onEnterSearchQuery: (String) -> Unit,
    sheetState: SheetState,
    currencies: List<CurrencyMetadata>,
    canContinue: Boolean = false,
    isEmpty: Boolean = false,
) {
    ModalBottomSheet(
        modifier = modifier.fillMaxHeight(),
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Box(
            modifier =
                Modifier
                    .padding(16.dp),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.select_a_currency),
                    style = MaterialTheme.typography.bodyMedium,
                )
                if (isEmpty) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Image(
                        imageVector = EmptyImage,
                        contentDescription = stringResource(R.string.empty_image),
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = stringResource(R.string.no_currencies_found),
                        style =
                            MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(0.7f),
                            ),
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }
                if (!isEmpty) {
                    Spacer(modifier = Modifier.height(32.dp))
                    CurrencyBuddySearchTextField(
                        modifier = Modifier.fillMaxWidth(),
                        hint = stringResource(id = R.string.search_for_currency),
                        searchQuery = searchQuery,
                        onEnterSearchQuery = onEnterSearchQuery,
                    )
                }
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    itemsIndexed(items = currencies, key = { _, item -> item.code }) { index, currency ->
                        SelectCurrencyItem(
                            modifier = Modifier.fillMaxWidth(),
                            imageUrl = currency.flag.svg,
                            text = "${currency.name}(${currency.code})",
                            selected = currency.isSelected,
                            onClick = { onSelectCurrency(index) },
                        )
                    }

                    item { Spacer(modifier = Modifier.height(56.dp)) }
                }
            }
            Column {
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    if (isEmpty) {
                        CurrencyBuddySecondaryButtonOutlined(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.cancel),
                            onClick = { onDismiss() },
                        )
                    }
                    CurrencyBuddyPrimaryButton(
                        modifier = Modifier.weight(1f),
                        text =
                            if (isEmpty) {
                                stringResource(R.string.retry)
                            } else {
                                stringResource(R.string.continue_text)
                            },
                        onClick = { if (isEmpty) onClickRetry() else onClickContinue() },
                        enabled = if (isEmpty) true else canContinue,
                    )
                }
            }
        }
    }
}

@PreviewLightDarkWithBackground
@Composable
private fun SelectCurrencyBottomSheetPreview() {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    LaunchedEffect(Unit) { sheetState.show() }
    CurrencybuddyTheme {
        Surface {
            SelectCurrencyBottomSheet(
                modifier = Modifier,
                onDismiss = { },
                searchQuery = "",
                onEnterSearchQuery = { },
                currencies = listOf(),
                onSelectCurrency = { },
                onClickContinue = { },
                onClickRetry = { },
                sheetState = sheetState,
            )
        }
    }
}

private val currenciesList =
    List(5) { index ->
        CurrencyMetadata(
            code = "USD$index",
            name = "United States Dollar $index",
            symbol = "$",
            flag = Flag(png = "", svg = ""),
            isSelected = index == 0,
        )
    }