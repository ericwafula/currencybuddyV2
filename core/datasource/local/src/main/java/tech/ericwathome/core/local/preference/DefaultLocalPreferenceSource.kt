package tech.ericwathome.core.local.preference

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.EmptyResult
import tech.ericwathome.core.domain.util.Result
import tech.ericwathome.core.domain.util.asEmptyDataResult

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
    ): EmptyResult<DataError.Local> {
        return try {
            dataStore.edit { it[key] = value }
            Result.Success(Unit).asEmptyDataResult()
        } catch (e: IOException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun <T> delete(key: Preferences.Key<T>) {
        dataStore.edit { it.remove(key) }
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}