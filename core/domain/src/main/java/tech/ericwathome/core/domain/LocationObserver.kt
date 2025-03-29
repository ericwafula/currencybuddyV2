package tech.ericwathome.core.domain

import kotlinx.coroutines.flow.Flow

interface LocationObserver {
    val getLiveLocation: Flow<Location>
}