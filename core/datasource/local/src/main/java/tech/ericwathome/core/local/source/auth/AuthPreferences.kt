package tech.ericwathome.core.local.source.auth

import kotlinx.coroutines.flow.Flow

interface AuthPreferences {
    val isOnboardingCompletedObservable: Flow<Boolean>

    suspend fun setIsOnboardingCompleted(value: Boolean)
}