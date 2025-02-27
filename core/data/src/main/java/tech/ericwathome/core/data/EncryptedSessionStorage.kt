package tech.ericwathome.core.data

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tech.ericwathome.core.domain.SessionStorage

class EncryptedSessionStorage(
    private val sharedPreferences: SharedPreferences,
) : SessionStorage {
    override suspend fun isOnboardingComplete(): Boolean {
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

    override suspend fun lastMetadataSyncTimestamp(): Long {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getLong(KEY_SYNCING_CURRENCY_META_DATA, 0)
        }
    }

    override suspend fun setLastMetadataSyncTimestamp(value: Long?) {
        withContext(Dispatchers.IO) {
            if (value == null) {
                sharedPreferences.edit().remove(KEY_SYNCING_CURRENCY_META_DATA).apply()
                return@withContext
            }

            sharedPreferences.edit().putLong(KEY_SYNCING_CURRENCY_META_DATA, value).apply()
        }
    }

    override suspend fun lastExchangeRateSyncTimestamp(): Long {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getLong(KEY_SYNCING_EXCHANGE_RATE, 0)
        }
    }

    override suspend fun setLastExchangeRateSyncTimestamp(value: Long?) {
        withContext(Dispatchers.IO) {
            if (value == null) {
                sharedPreferences.edit().remove(KEY_SYNCING_EXCHANGE_RATE).apply()
                return@withContext
            }

            sharedPreferences.edit().putLong(KEY_SYNCING_EXCHANGE_RATE, value).apply()
        }
    }

    companion object {
        private const val KEY_ONBOARDING_COMPLETE = "onboarding_complete"
        private const val KEY_SYNCING_CURRENCY_META_DATA = "syncing_currency_meta_data"
        private const val KEY_SYNCING_EXCHANGE_RATE = "syncing_exchange_rate"
    }
}