package tech.ericwathome.core.local.source.auth

import kotlinx.coroutines.flow.Flow
import tech.ericwathome.auth.domain.AuthLocalDataSource

internal class DefaultAuthLocalDataSource(
    private val authPreferences: AuthPreferences,
) : AuthLocalDataSource {
    override val isOnboardingCompletedObservable: Flow<Boolean>
        get() = authPreferences.isOnboardingCompletedObservable

    override suspend fun setIsOnboardingCompleted(value: Boolean) {
        authPreferences.setIsOnboardingCompleted(value)
    }
}