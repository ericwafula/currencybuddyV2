package tech.ericwathome.auth.domain

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isOnboardingCompletedObservable: Flow<Boolean>

    suspend fun setIsOnboardingCompleted(value: Boolean)
}