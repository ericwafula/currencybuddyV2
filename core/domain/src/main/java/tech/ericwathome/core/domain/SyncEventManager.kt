package tech.ericwathome.core.domain

import kotlinx.coroutines.flow.Flow

interface SyncEventManager {
    val event: Flow<SyncEvent>

    fun onEvent(event: SyncEvent)

    sealed interface SyncEvent {
        data object SyncMetadataSuccess : SyncEvent

        data object SyncMetadataError : SyncEvent
    }
}