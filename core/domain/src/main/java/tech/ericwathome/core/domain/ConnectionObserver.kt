package tech.ericwathome.core.domain

import kotlinx.coroutines.flow.StateFlow

interface ConnectionObserver {
    val hasNetworkConnection: StateFlow<Boolean>
}