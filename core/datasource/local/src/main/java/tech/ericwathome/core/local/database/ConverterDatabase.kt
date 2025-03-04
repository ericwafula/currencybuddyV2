package tech.ericwathome.core.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import tech.ericwathome.core.local.model.entity.CurrencyMetadataEntity
import tech.ericwathome.core.local.model.entity.ExchangeRateEntity
import tech.ericwathome.core.local.source.converter.ConverterDao

@Database(
    version = 1,
    entities = [
        ExchangeRateEntity::class,
        CurrencyMetadataEntity::class,
    ],
)
abstract class ConverterDatabase : RoomDatabase() {
    abstract val converterDao: ConverterDao
}