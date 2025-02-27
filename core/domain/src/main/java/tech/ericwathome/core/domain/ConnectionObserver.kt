package tech.ericwathome.core.domain

import kotlinx.coroutines.flow.Flow

interface ConnectionObserver {
    fun isAvailable(): Flow<Boolean>
}