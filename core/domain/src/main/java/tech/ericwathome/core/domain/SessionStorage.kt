package tech.ericwathome.core.domain

interface SessionStorage {
    suspend fun isOnboardingComplete(): Boolean

    suspend fun setOnboardingComplete(value: Boolean?)

    suspend fun lastMetadataSyncTimestamp(): Long

    suspend fun setLastMetadataSyncTimestamp(value: Long?)

    suspend fun lastExchangeRateSyncTimestamp(): Long

    suspend fun setLastExchangeRateSyncTimestamp(value: Long?)
}