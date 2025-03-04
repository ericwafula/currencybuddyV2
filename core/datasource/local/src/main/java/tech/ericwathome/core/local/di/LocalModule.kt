@file:Keep

package tech.ericwathome.core.local.di

import android.content.Context
import androidx.annotation.Keep
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import tech.ericwathome.auth.domain.AuthLocalDataSource
import tech.ericwathome.core.domain.converter.LocalConverterDataSource
import tech.ericwathome.core.local.database.ConverterDatabase
import tech.ericwathome.core.local.preference.DefaultLocalPreferenceSource
import tech.ericwathome.core.local.preference.LocalPreferenceSource
import tech.ericwathome.core.local.source.auth.AuthPreferences
import tech.ericwathome.core.local.source.auth.DefaultAuthLocalDataSource
import tech.ericwathome.core.local.source.auth.DefaultAuthPreferences
import tech.ericwathome.core.local.source.converter.ConverterPreferences
import tech.ericwathome.core.local.source.converter.DefaultConverterPreferences
import tech.ericwathome.core.local.source.converter.DefaultLocalConverterDataSource

val Context.datastore: DataStore<Preferences> by preferencesDataStore(
    name = "currencybuddy.local",
    corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
)

val localModule =
    module {
        single {
            Room.databaseBuilder(
                androidApplication(),
                ConverterDatabase::class.java,
                "converter.db",
            ).build()
        }
        single { get<ConverterDatabase>().converterDao }

        singleOf(::DefaultLocalConverterDataSource).bind<LocalConverterDataSource>()

        single<DataStore<Preferences>>(named("local")) { get<Context>().datastore }

        single<LocalPreferenceSource> { DefaultLocalPreferenceSource(get(named("local"))) }

        singleOf(::DefaultConverterPreferences).bind<ConverterPreferences>()

        singleOf(::DefaultAuthLocalDataSource).bind<AuthLocalDataSource>()

        singleOf(::DefaultAuthPreferences).bind<AuthPreferences>()
    }