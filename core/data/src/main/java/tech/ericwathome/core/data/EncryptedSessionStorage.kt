package tech.ericwathome.core.data

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tech.ericwathome.core.domain.SessionStorage

class EncryptedSessionStorage(
    private val sharedPreferences: SharedPreferences,
) : SessionStorage {
    override suspend fun isOnboardingComplete(): Boolean? {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getBoolean(KEY_ONBOARDING_COMPLETE, false)
        }
    }

    override suspend fun setOnboardingComplete(value: Boolean?) {
        withContext(Dispatchers.IO) {
            if (value == null) {
                sharedPreferences.edit().remove(KEY_ONBOARDING_COMPLETE).apply()
                return@withContext
            }

            sharedPreferences.edit().putBoolean(KEY_ONBOARDING_COMPLETE, value).apply()
        }
    }

    companion object {
        private const val KEY_ONBOARDING_COMPLETE = "onboarding_complete"
    }
}