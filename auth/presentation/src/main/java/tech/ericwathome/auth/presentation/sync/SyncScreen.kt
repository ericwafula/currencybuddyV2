@file:OptIn(ExperimentalSharedTransitionApi::class)

package tech.ericwathome.auth.presentation.sync

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import tech.ericwathome.auth.presentation.R
import tech.ericwathome.core.presentation.designsystem.CurrencybuddyTheme
import tech.ericwathome.core.presentation.designsystem.LogoCompact
import tech.ericwathome.core.presentation.designsystem.utils.PreviewLightDarkWithBackground
import tech.ericwathome.core.presentation.designsystem.utils.WithSharedTransitionScope
import tech.ericwathome.core.presentation.ui.CollectOneTimeEvent
import tech.ericwathome.core.presentation.ui.showToastIfNeeded

@Composable
fun SyncScreen(
    onNavigateToGetStarted: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SyncViewModel = koinViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()
    val context = LocalContext.current

    CollectOneTimeEvent(viewModel.event) { event ->
        when (event) {
            is SyncEvent.OnNavigateToGetStarted -> {
                context.showToastIfNeeded(event.uiText, event.showToast)

                onNavigateToGetStarted()
            }

            is SyncEvent.OnNavigateToHome -> {
                context.showToastIfNeeded(event.uiText, event.showToast)

                onNavigateToHome()
            }
        }
    }

    WithSharedTransitionScope {
        SyncScreenContent(
            isSyncing = isSyncing,
            animatedVisibilityScope = animatedVisibilityScope,
        )
    }
}

@Composable
private fun SharedTransitionScope.SyncScreenContent(
    isSyncing: Boolean,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    Surface {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .sharedElement(
                        state = rememberSharedContentState("sync_screen"),
                        animatedVisibilityScope = animatedVisibilityScope,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier = Modifier,
                    imageVector = LogoCompact,
                    contentDescription = stringResource(R.string.currency_buddy_logo),
                )
                if (isSyncing) {
                    Spacer(modifier = Modifier.height(24.dp))
                    LinearProgressIndicator(
                        modifier = Modifier.width(118.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.syncing_currency_data),
                        style =
                            MaterialTheme.typography.bodySmall.copy(
                                color =
                                    MaterialTheme.colorScheme.onSurface.copy(
                                        alpha = 0.6f,
                                    ),
                            ),
                    )
                }
            }
        }
    }
}

@PreviewLightDarkWithBackground
@Composable
private fun SyncScreenPreview() {
    CurrencybuddyTheme {
        WithSharedTransitionScope {
            AnimatedVisibility(true) {
                SyncScreenContent(
                    isSyncing = true,
                    animatedVisibilityScope = this,
                )
            }
        }
    }
}