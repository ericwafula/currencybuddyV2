package tech.ericwathome.auth.presentation.sync

import tech.ericwathome.core.presentation.ui.UiText

sealed interface SyncEvent {
    data object OnSuccess : SyncEvent

    data class OnError(val message: UiText) : SyncEvent
}