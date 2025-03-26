package tech.ericwathome.converter.data

import androidx.annotation.Keep
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import tech.ericwathome.core.domain.SyncEventManager

@Keep
internal class DefaultSyncEventManager(
    private val scope: CoroutineScope,
) : SyncEventManager {
    private val _event = MutableSharedFlow<SyncEventManager.SyncEvent>()
    override val event = _event.asSharedFlow()

    override fun onEvent(event: SyncEventManager.SyncEvent) {
        scope.launch { _event.emit(event) }
    }
}