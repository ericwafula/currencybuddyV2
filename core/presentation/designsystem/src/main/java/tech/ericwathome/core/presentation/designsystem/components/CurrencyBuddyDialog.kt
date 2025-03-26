package tech.ericwathome.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import tech.ericwathome.core.presentation.designsystem.CurrencybuddyTheme
import tech.ericwathome.core.presentation.designsystem.R
import tech.ericwathome.core.presentation.designsystem.utils.PreviewLightDarkWithBackground

@Composable
fun CurrencyBuddyDialog(
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit,
    onDismiss: () -> Unit,
    title: String,
    description: String,
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Box(
            modifier =
                modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface),
        ) {
            Box(
                modifier =
                    modifier
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
            ) {
                Column(
                    modifier =
                        modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = title,
                        style =
                            MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                            ),
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = description,
                        style =
                            MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                            ),
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        actions()
                    }
                }
            }
        }
    }
}

@PreviewLightDarkWithBackground
@Composable
private fun CurrencyBuddyDialogPreview() {
    CurrencybuddyTheme {
        CurrencyBuddyDialog(
            title = stringResource(R.string.permission_required),
            description = stringResource(R.string.notification_permission_description).trimIndent(),
            actions = {
                CurrencyBuddyPrimaryButton(
                    modifier = Modifier.weight(1f),
                    onClick = { },
                    text = stringResource(R.string.okay),
                )
            },
            onDismiss = { },
        )
    }
}