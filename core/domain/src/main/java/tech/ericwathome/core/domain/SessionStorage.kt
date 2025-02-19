package tech.ericwathome.core.domain

interface SessionStorage {
    suspend fun isOnboardingComplete(): Boolean?

    suspend fun setOnboardingComplete(value: Boolean?)
}