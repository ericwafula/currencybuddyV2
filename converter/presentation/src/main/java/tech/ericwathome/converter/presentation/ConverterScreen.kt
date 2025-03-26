@file:Keep
@file:OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)

package tech.ericwathome.converter.presentation

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Keep
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import tech.ericwathome.converter.presentation.components.ConverterKeyboard
import tech.ericwathome.converter.presentation.components.CurrencyPickerButton
import tech.ericwathome.converter.presentation.components.SelectCurrencyBottomSheet
import tech.ericwathome.converter.presentation.utils.hasLocationPermissions
import tech.ericwathome.converter.presentation.utils.hasNotificationPermission
import tech.ericwathome.converter.presentation.utils.shouldShowLocationPermissionRationale
import tech.ericwathome.converter.presentation.utils.shouldShowNotificationPermissionRationale
import tech.ericwathome.core.presentation.designsystem.CurrencybuddyTheme
import tech.ericwathome.core.presentation.designsystem.assets.SwapIcon
import tech.ericwathome.core.presentation.designsystem.components.CurrencyBuddyCenteredTopBarLayout
import tech.ericwathome.core.presentation.designsystem.components.CurrencyBuddyDialog
import tech.ericwathome.core.presentation.designsystem.components.CurrencyBuddyPrimaryButton
import tech.ericwathome.core.presentation.designsystem.utils.PreviewLightDarkWithBackground
import tech.ericwathome.core.presentation.designsystem.utils.WithSharedTransitionScope
import tech.ericwathome.core.presentation.designsystem.utils.shimmerEffect
import tech.ericwathome.core.presentation.ui.CollectOneTimeEvent
import tech.ericwathome.core.presentation.ui.SharedContentKeys
import tech.ericwathome.core.presentation.ui.showToast

@Composable
fun ConverterScreen(
    viewModel: ConverterViewModel = koinViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    CollectOneTimeEvent(viewModel.event) { event ->
        when (event) {
            is ConverterEvent.ShowToast -> context.showToast(event.message.asString(context))
            ConverterEvent.DismissSnackbar -> snackbarHostState.currentSnackbarData?.dismiss()
            is ConverterEvent.ShowSnackbar ->
                snackbarHostState.showSnackbar(
                    message = event.message.asString(context),
                    actionLabel = event.actionLabel.asString(context),
                )
        }
    }

    WithSharedTransitionScope {
        ConverterScreenContent(
            state = state,
            animatedVisibilityScope = animatedVisibilityScope,
            snackbarHostState = snackbarHostState,
            sheetState = sheetState,
            onAction = viewModel::onAction,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SharedTransitionScope.ConverterScreenContent(
    state: ConverterState,
    animatedVisibilityScope: AnimatedVisibilityScope,
    snackbarHostState: SnackbarHostState,
    sheetState: SheetState,
    onAction: (ConverterAction) -> Unit,
) {
    val surfaceTextErrorColor by animateColorAsState(
        targetValue =
            if (state.isError) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onSecondary
            },
    )
    val context = LocalContext.current
    val activity = remember { context as? ComponentActivity }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
        ) { perms ->
            val hasGrantedNotificationPermission =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    perms[Manifest.permission.POST_NOTIFICATIONS] == true
                } else {
                    true
                }
            val hasGrantedLocationPermissions = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true

            val showNotificationRationale = activity?.shouldShowNotificationPermissionRationale() ?: true
            val showLocationRationale = activity?.shouldShowLocationPermissionRationale() ?: true

            onAction(
                ConverterAction.SubmitLocationPermissionInfo(
                    permissionGranted = hasGrantedLocationPermissions,
                    showLocationRationale = showLocationRationale,
                ),
            )

            onAction(
                ConverterAction.SubmitNotificationPermissionInfo(
                    permissionGranted = hasGrantedNotificationPermission,
                    showNotificationRationale = showNotificationRationale,
                ),
            )
        }

    LaunchedEffect(true) {
        val showNotificationRationale = activity?.shouldShowNotificationPermissionRationale() ?: true
        val showLocationRationale = activity?.shouldShowLocationPermissionRationale() ?: true

        val hasNotificationPermission = context.hasNotificationPermission()
        val hasLocationPermissions = context.hasLocationPermissions()

        onAction(
            ConverterAction.SubmitLocationPermissionInfo(
                permissionGranted = hasNotificationPermission,
                showLocationRationale = showLocationRationale,
            ),
        )

        onAction(
            ConverterAction.SubmitNotificationPermissionInfo(
                permissionGranted = hasLocationPermissions,
                showNotificationRationale = showNotificationRationale,
            ),
        )

        if (!hasNotificationPermission || !hasLocationPermissions) {
            permissionLauncher.requestMultiplePermissions(context)
        }
    }

    CurrencyBuddyCenteredTopBarLayout(
        modifier = Modifier.fillMaxSize(),
        toolbarTitle = stringResource(id = R.string.currency_converter),
        colors =
            TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                titleContentColor = MaterialTheme.colorScheme.onSecondary,
                actionIconContentColor = MaterialTheme.colorScheme.onSecondary,
            ),
        snackbarHostState = snackbarHostState,
    ) {
        PullToRefreshBox(
            isRefreshing = state.isSyncingCurrencies,
            onRefresh = { onAction(ConverterAction.OnRefresh) },
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(32.dp),
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                ) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(bottomStart = 60.dp, bottomEnd = 60.dp))
                                .background(color = MaterialTheme.colorScheme.secondary)
                                .padding(16.dp)
                                .sharedElement(
                                    state = rememberSharedContentState(key = SharedContentKeys.GET_STARTED_SECONDARY_COLOR),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                ),
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
                                        lineHeight = 64.sp,
                                    ),
                            )
                            Text(
                                modifier = Modifier
                                    .then(
                                        if(state.isSyncingConversionRates) {
                                            Modifier.shimmerEffect()
                                        } else {
                                            Modifier
                                        }
                                    ),
                                text = "= ${state.result} ${state.quoteCurrencyCode}",
                                style =
                                    MaterialTheme.typography.titleLarge.copy(
                                        fontSize = 24.sp,
                                        color = surfaceTextErrorColor,
                                    ),
                            )
                        }
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .align(Alignment.BottomCenter),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            CurrencyPickerButton(
                                imageUrl = state.baseFlagUrl,
                                text = state.baseCurrencyCode,
                                onClick = { onAction(ConverterAction.OnClickBaseButton) },
                            )
                            IconButton(onClick = { onAction(ConverterAction.OnClickSwapButton) }) {
                                Icon(
                                    imageVector = SwapIcon,
                                    contentDescription = stringResource(R.string.swap_icon),
                                    tint = MaterialTheme.colorScheme.onSecondary,
                                )
                            }
                            CurrencyPickerButton(
                                imageUrl = state.quoteFlagUrl,
                                text = state.quoteCurrencyCode,
                                onClick = { onAction(ConverterAction.OnClickQuoteButton) },
                            )
                        }

                        androidx.compose.animation.AnimatedVisibility(
                            modifier = Modifier.align(Alignment.Center),
                            visible = state.isError,
                        ) {
                            Text(
                                text = state.errorMessage?.asString() ?: "",
                                style =
                                    MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSecondary,
                                        fontSize = 12.sp,
                                    ),
                            )
                        }
                    }

                    androidx.compose.animation.AnimatedVisibility(
                        modifier = Modifier.align(Alignment.Center),
                        visible = state.isError,
                    ) {
                        Text(
                            text = state.errorMessage?.asString() ?: "",
                            style =
                                MaterialTheme.typography.bodySmall.copy(
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

    if (state.showCurrencyPickerBottomSheet) {
        SelectCurrencyBottomSheet(
            modifier = Modifier,
            sheetState = sheetState,
            onSelectCurrency = { onAction(ConverterAction.OnSelectCurrency(it)) },
            onDismiss = { onAction(ConverterAction.OnDismissBottomSheet) },
            onClickContinue = { onAction(ConverterAction.OnClickContinue) },
            searchQuery = state.searchQuery,
            onEnterSearchQuery = { onAction(ConverterAction.OnEnterSearchQuery(it)) },
            filteredCurrencyList = state.currencyMetadataList,
            onClickRetry = { onAction(ConverterAction.OnRefresh) },
            canContinue = state.canContinue && !state.isSearching,
            isSearching = state.isSearching,
            isOriginalCurrencyListEmpty = state.isOriginalCurrencyListEmpty,
        )
    }

    if (state.showLocationRationale || state.showNotificationRationale) {
        CurrencyBuddyDialog(
            title = stringResource(tech.ericwathome.core.presentation.designsystem.R.string.permission_required),
            description =
                when {
                    state.showNotificationRationale && state.showLocationRationale -> {
                        stringResource(
                            tech.ericwathome.core.presentation.designsystem.R.string.location_and_notification_permissions_description,
                        ).trimIndent()
                    }
                    state.showNotificationRationale -> {
                        stringResource(
                            tech.ericwathome.core.presentation.designsystem.R.string.notification_permission_description,
                        ).trimIndent()
                    }
                    else -> {
                        stringResource(
                            tech.ericwathome.core.presentation.designsystem.R.string.location_permission_description,
                        ).trimIndent()
                    }
                },
            actions = {
                CurrencyBuddyPrimaryButton(
                    modifier = Modifier.weight(1f),
                    onClick = { permissionLauncher.requestMultiplePermissions(context) },
                    text = stringResource(tech.ericwathome.core.presentation.designsystem.R.string.okay),
                )
            },
            onDismiss = { /* Do nothing */ },
        )
    }
}

fun ActivityResultLauncher<Array<String>>.requestMultiplePermissions(context: Context) {
    val hasLocationPermissions = context.hasLocationPermissions()
    val hasNotificationPermissions = context.hasNotificationPermission()

    val locationPermissions =
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )

    val notificationPermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            emptyArray()
        }

    when {
        !hasLocationPermissions && !hasNotificationPermissions -> {
            launch(locationPermissions + notificationPermission)
        }
        !hasLocationPermissions -> {
            launch(locationPermissions)
        }
        !hasNotificationPermissions -> {
            launch(notificationPermission)
        }
    }
}

@PreviewLightDarkWithBackground
@Composable
private fun ConverterScreenPreview() {
    CurrencybuddyTheme {
        Surface {
            WithSharedTransitionScope {
                AnimatedVisibility(true) {
                    ConverterScreenContent(
                        state =
                            ConverterState(
                                isSyncingCurrencies = true,
                            ),
                        onAction = { },
                        animatedVisibilityScope = this,
                        snackbarHostState = SnackbarHostState(),
                        sheetState = rememberModalBottomSheetState(),
                    )
                }
            }
        }
    }
}