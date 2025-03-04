package tech.ericwathome.auth.data

import kotlinx.coroutines.flow.Flow
import tech.ericwathome.auth.domain.AuthLocalDataSource
import tech.ericwathome.auth.domain.AuthRepository

class DefaultAuthRepository(
    private val authLocalDataSource: AuthLocalDataSource,
) : AuthRepository {
    override val isOnboardingCompletedObservable: Flow<Boolean>
        get() = authLocalDataSource.isOnboardingCompletedObservable

    override suspend fun setIsOnboardingCompleted(value: Boolean) {
        authLocalDataSource.setIsOnboardingCompleted(value)
    }
}