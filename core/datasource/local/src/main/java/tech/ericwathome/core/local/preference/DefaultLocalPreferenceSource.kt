package tech.ericwathome.core.local.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class DefaultLocalPreferenceSource(
    private val dataStore: DataStore<Preferences>,
) : LocalPreferenceSource {
    override fun <T> getNullable(key: Preferences.Key<T>): Flow<T?> {
        return dataStore.data.map { it[key] }
    }

    override fun <T> get(
        key: Preferences.Key<T>,
        default: T,
    ): Flow<T> {
        return dataStore.data.map { it[key] ?: default }
    }

    override suspend fun <T> saveOrUpdate(
        key: Preferences.Key<T>,
        value: T,
    ) {
        dataStore.edit { it[key] = value }
    }

    override suspend fun <T> delete(key: Preferences.Key<T>) {
        dataStore.edit { it.remove(key) }
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}