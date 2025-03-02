package tech.ericwathome.core.domain

import kotlinx.coroutines.flow.Flow

interface ConnectionObserver {
    val hasNetworkConnection: Flow<Boolean>
}