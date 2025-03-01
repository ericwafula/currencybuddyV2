@file:Keep

package tech.ericwathome.converter.presentation

import androidx.annotation.Keep
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import tech.ericwathome.converter.presentation.components.ConverterKeyboard
import tech.ericwathome.core.presentation.designsystem.CurrencybuddyTheme
import tech.ericwathome.core.presentation.designsystem.components.CurrencyBuddyCenteredTopBarLayout
import tech.ericwathome.core.presentation.designsystem.utils.PreviewLightDarkWithBackground

@Composable
fun ConverterScreen(viewModel: ConverterViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ConverterScreenContent(
        state = state,
        onAction = viewModel::onAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConverterScreenContent(
    state: ConverterState,
    onAction: (ConverterAction) -> Unit,
) {
    CurrencyBuddyCenteredTopBarLayout(
        modifier = Modifier.fillMaxSize(),
        toolbarTitle = stringResource(id = R.string.currency_converter),
        colors =
            TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                titleContentColor = MaterialTheme.colorScheme.onSecondary,
                actionIconContentColor = MaterialTheme.colorScheme.onSecondary,
            ),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(bottomStart = 60.dp, bottomEnd = 60.dp))
                        .background(color = MaterialTheme.colorScheme.secondary)
                        .padding(16.dp),
            ) {
                Column(
                    modifier = Modifier.align(Alignment.TopStart),
                ) {
                    Text(
                        text = "${state.amount} ${state.baseCurrencyCode}",
                        style =
                            MaterialTheme.typography.titleLarge.copy(
                                fontSize = 64.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondary,
                            ),
                    )
                    Text(
                        text = "= ${state.result} ${state.quoteCurrencyCode}",
                        style =
                            MaterialTheme.typography.titleLarge.copy(
                                fontSize = 24.sp,
                                color = MaterialTheme.colorScheme.onSecondary,
                            ),
                    )
                }
            }
            ConverterKeyboard(
                modifier = Modifier.fillMaxWidth(),
                onClickInputKey = { onAction(ConverterAction.OnEnterInput(it)) },
                onClickConvert = { onAction(ConverterAction.OnClickConvert) },
                onClickDelete = { onAction(ConverterAction.OnDeleteInput) },
                onClickClear = { onAction(ConverterAction.OnClearInput) },
            )
        }
    }
}

@PreviewLightDarkWithBackground
@Composable
private fun ConverterScreenPreview() {
    CurrencybuddyTheme {
        Surface {
            ConverterScreenContent(
                state = ConverterState(),
                onAction = { },
            )
        }
    }
}