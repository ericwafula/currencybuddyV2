package tech.ericwathome.core.database.di

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import tech.ericwathome.core.database.ConverterDatabase
import tech.ericwathome.core.database.RoomLocalConverterDataSource
import tech.ericwathome.core.domain.converter.LocalConverterDataSource

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            ConverterDatabase::class.java,
            "converter.db"
        ).build()
    }
    single { get<ConverterDatabase>().converterDao }

    singleOf(::RoomLocalConverterDataSource).bind<LocalConverterDataSource>()
}