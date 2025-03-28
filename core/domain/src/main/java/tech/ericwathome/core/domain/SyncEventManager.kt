package tech.ericwathome.core.domain

import kotlinx.coroutines.flow.SharedFlow

interface SyncEventManager {
    val event: SharedFlow<SyncEvent>

    fun onEvent(event: SyncEvent)

    sealed interface SyncEvent {
        data object SyncMetadataSuccess : SyncEvent

        data object SyncMetadataError : SyncEvent

        data object SyncSelectedCurrencyPairSuccess : SyncEvent

        data object SyncSelectedCurrencyPairError : SyncEvent
    }
}