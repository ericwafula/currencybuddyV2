package tech.ericwathome.core.domain

import kotlinx.coroutines.flow.Flow

interface ConnectionObserver {
    enum class ConnectionState {
        CONNECTED,
        DISCONNECTED,
    }

    fun observe(): Flow<ConnectionState>
}