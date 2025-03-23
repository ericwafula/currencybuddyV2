package tech.ericwathome.converter.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import tech.ericwathome.core.domain.SyncEventManager

internal class DefaultSyncEventManager(
    private val scope: CoroutineScope,
) : SyncEventManager {
    private val _event = Channel<SyncEventManager.SyncEvent>()
    override val event = _event.receiveAsFlow()

    override fun onEvent(event: SyncEventManager.SyncEvent) {
        scope.launch { _event.send(event) }
    }
}