package tech.ericwathome.core.local.source.auth

import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.Flow
import tech.ericwathome.core.local.preference.LocalPreferenceSource

internal class DefaultAuthPreferences(
    private val preferenceSource: LocalPreferenceSource
) : AuthPreferences {
    object Keys {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_complete")
    }

    override val isOnboardingCompletedObservable: Flow<Boolean>
        get() = preferenceSource.get(Keys.ONBOARDING_COMPLETED, false)

    override suspend fun setIsOnboardingCompleted(value: Boolean) {
        preferenceSource.saveOrUpdate(Keys.ONBOARDING_COMPLETED, value)
    }
}