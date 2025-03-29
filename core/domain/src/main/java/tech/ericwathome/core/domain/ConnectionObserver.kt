package tech.ericwathome.core.domain

import kotlinx.coroutines.flow.SharedFlow

interface ConnectionObserver {
    val networkStatus: SharedFlow<NetworkStatus>

    sealed interface NetworkStatus {
        data object Available : NetworkStatus

        data object Unavailable : NetworkStatus

        data object Checking : NetworkStatus
    }
}