package tech.ericwathome.core.local.preference

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.EmptyResult

interface LocalPreferenceSource {
    fun <T> getNullable(key: Preferences.Key<T>): Flow<T?>

    fun <T> get(
        key: Preferences.Key<T>,
        default: T,
    ): Flow<T>

    suspend fun <T> saveOrUpdate(
        key: Preferences.Key<T>,
        value: T,
    ): EmptyResult<DataError.Local>

    suspend fun <T> delete(key: Preferences.Key<T>)

    suspend fun clear()
}