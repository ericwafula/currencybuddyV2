@file:[Keep OptIn(ExperimentalSharedTransitionApi::class)]

package tech.ericwathome.auth.presentation.getstarted

import androidx.annotation.Keep
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import tech.ericwathome.auth.presentation.R
import tech.ericwathome.auth.presentation.assets.GetStartedCurrencyImage
import tech.ericwathome.core.presentation.designsystem.CurrencybuddyTheme
import tech.ericwathome.core.presentation.designsystem.assets.LogoFull
import tech.ericwathome.core.presentation.designsystem.components.PrimaryButton
import tech.ericwathome.core.presentation.designsystem.utils.PreviewLightDarkWithBackground
import tech.ericwathome.core.presentation.designsystem.utils.WithSharedTransitionScope
import tech.ericwathome.core.presentation.ui.SharedContentKeys

@Composable
fun GetStartedScreen(
    onNavigateToHome: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    WithSharedTransitionScope {
        GetStartedScreenContent(
            onNavigateToHome = onNavigateToHome,
            animatedVisibilityScope = animatedVisibilityScope,
        )
    }
}

@Composable
fun SharedTransitionScope.GetStartedScreenContent(
    onNavigateToHome: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    Surface {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier =
                    Modifier.sharedElement(
                        state = rememberSharedContentState(key = SharedContentKeys.SYNC_SCREEN_LOGO),
                        animatedVisibilityScope = animatedVisibilityScope,
                    ),
                imageVector = LogoFull,
                contentDescription = stringResource(tech.ericwathome.core.presentation.ui.R.string.logo),
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.your_ultimate_currency_companion),
                style =
                    MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center,
                    ),
            )
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    imageVector = GetStartedCurrencyImage,
                    contentDescription = stringResource(R.string.currency_illustration),
                )
                Spacer(modifier = Modifier.height(100.dp))
                Text(
                    text =
                        stringResource(
                            R.string.currency_buddy_is_your_trusted_companion_for_managing_currencies,
                        ),
                    style =
                        MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center,
                        ),
                )
            }
            PrimaryButton(
                modifier = Modifier,
                onClick = onNavigateToHome,
                text = stringResource(R.string.get_started),
            )
        }
    }
}

@PreviewLightDarkWithBackground
@Composable
private fun GetStartedScreenPreview() {
    CurrencybuddyTheme {
        WithSharedTransitionScope {
            AnimatedVisibility(true) {
                GetStartedScreenContent(
                    onNavigateToHome = {},
                    animatedVisibilityScope = this,
                )
            }
        }
    }
}