package tech.ericwathome.auth.presentation.sync

import tech.ericwathome.core.presentation.ui.UiText

sealed interface SyncEvent {
    data class OnNavigateToHome(val uiText: UiText? = null, val showToast: Boolean) : SyncEvent

    data class OnNavigateToGetStarted(val uiText: UiText? = null, val showToast: Boolean) : SyncEvent
}