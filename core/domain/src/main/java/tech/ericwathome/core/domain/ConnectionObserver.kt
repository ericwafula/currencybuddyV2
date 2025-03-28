package tech.ericwathome.core.domain

import kotlinx.coroutines.flow.StateFlow

interface ConnectionObserver {
    val hasNetworkConnection: StateFlow<NetworkStatus>

    sealed interface NetworkStatus {
        data object Available : NetworkStatus

        data object Unavailable : NetworkStatus

        data object Checking : NetworkStatus
    }
}