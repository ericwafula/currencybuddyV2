package tech.ericwathome.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tech.ericwathome.core.presentation.designsystem.CurrencybuddyTheme
import tech.ericwathome.core.presentation.designsystem.R
import tech.ericwathome.core.presentation.designsystem.utils.PreviewLightDarkWithBackground

@Composable
fun CurrencyBuddyTinyPopUp(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    isVisible: Boolean,
    text: String,
) {
    Box(
        modifier =
            modifier
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp),
    ) {
    }
}

@PreviewLightDarkWithBackground
@Composable
private fun CurrencyBuddyTinyPopUpPreview() {
    CurrencybuddyTheme {
        CurrencyBuddyTinyPopUp(
            onDismiss = {},
            onAccept = {},
            onDecline = {},
            isVisible = true,
            text = stringResource(R.string.would_you_like_to_add_this_to_your_favourites),
        )
    }
}