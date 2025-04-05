package tech.ericwathome.core.domain

import kotlinx.coroutines.flow.SharedFlow

interface ConnectionObserver {
    val networkStatus: SharedFlow<Boolean>
}