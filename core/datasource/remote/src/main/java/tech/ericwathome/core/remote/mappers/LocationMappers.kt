package tech.ericwathome.core.remote.mappers

import android.location.Location

fun Location.toDomain(): tech.ericwathome.core.domain.Location {
    return tech.ericwathome.core.domain.Location(
        lat = latitude,
        lon = longitude,
    )
}