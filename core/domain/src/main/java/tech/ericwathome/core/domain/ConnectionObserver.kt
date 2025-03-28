package tech.ericwathome.core.domain

import kotlinx.coroutines.flow.StateFlow

interface ConnectionObserver {
    val networkStatus: StateFlow<NetworkStatus>

    sealed interface NetworkStatus {
        data object Available : NetworkStatus

        data object Unavailable : NetworkStatus

        data object Checking : NetworkStatus
    }
}