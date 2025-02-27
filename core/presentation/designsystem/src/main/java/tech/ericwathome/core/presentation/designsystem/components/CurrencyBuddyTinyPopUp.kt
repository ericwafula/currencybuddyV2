package tech.ericwathome.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import tech.ericwathome.core.presentation.designsystem.CurrencybuddyTheme
import tech.ericwathome.core.presentation.designsystem.R
import tech.ericwathome.core.presentation.designsystem.utils.PreviewLightDarkWithBackground

@Composable
fun CurrencyBuddyTinyPopUp(
    modifier: Modifier = Modifier,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    isVisible: Boolean,
    text: String,
    dismissText: String,
    acceptText: String,
    showDeclineOption: Boolean = true,
) {
    if (isVisible) {
        Box(
            modifier =
                modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = text,
                    style =
                        MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onPrimary,
                        ),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement =
                        Arrangement.spacedBy(
                            space = 8.dp,
                            alignment = Alignment.End,
                        ),
                ) {
                    if (showDeclineOption) {
                        TextButton(
                            onClick = onDecline,
                        ) {
                            Text(
                                text = dismissText,
                                style =
                                    MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onPrimary,
                                    ),
                            )
                        }
                    }

                    TextButton(
                        onClick = onAccept,
                    ) {
                        Text(
                            text = acceptText,
                            style =
                                MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontWeight = FontWeight.Bold,
                                ),
                        )
                    }
                }
            }
        }
    }
}

@PreviewLightDarkWithBackground
@Composable
private fun CurrencyBuddyTinyPopUpPreview() {
    CurrencybuddyTheme {
        CurrencyBuddyTinyPopUp(
            onAccept = {},
            onDecline = {},
            isVisible = true,
            text = stringResource(R.string.would_you_like_to_add_this_to_your_favourites),
            dismissText = stringResource(R.string.no),
            acceptText = stringResource(R.string.yes),
            showDeclineOption = true,
        )
    }
}