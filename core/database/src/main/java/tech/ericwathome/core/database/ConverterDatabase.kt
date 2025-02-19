package tech.ericwathome.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import tech.ericwathome.core.database.dao.ConverterDao
import tech.ericwathome.core.database.entity.CurrencyPairEntity

@Database(
    version = 1,
    entities = [
        CurrencyPairEntity::class,
    ],
)
abstract class ConverterDatabase : RoomDatabase() {
    abstract val converterDao: ConverterDao
}