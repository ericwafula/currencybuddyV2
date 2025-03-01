package tech.ericwathome.core.database

import android.database.sqlite.SQLiteFullException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tech.ericwathome.core.database.dao.ConverterDao
import tech.ericwathome.core.database.mappers.toDomain
import tech.ericwathome.core.database.mappers.toEntity
import tech.ericwathome.core.domain.converter.LocalConverterDataSource
import tech.ericwathome.core.domain.converter.model.CurrencyMetadata
import tech.ericwathome.core.domain.converter.model.ExchangeRate
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.EmptyResult
import tech.ericwathome.core.domain.util.Result

class RoomLocalConverterDataSource(
    private val converterDao: ConverterDao,
) : LocalConverterDataSource {
    override fun observeDefaultExchangeRate(): Flow<ExchangeRate> {
        return converterDao.observeDefaultExchangeRate().map { it.toDomain() }
    }

    override fun observeNonDefaultExchangeRates(): Flow<List<ExchangeRate>> {
        return converterDao.observeNonDefaultExchangeRates().map { exchangeRateEntities ->
            exchangeRateEntities.map { it.toDomain() }
        }
    }

    override suspend fun retrieveSavedExchangeRates(): List<ExchangeRate> {
        return converterDao.retrieveSavedExchangeRates().map { it.toDomain() }
    }

    override suspend fun upsertLocalExchangeRate(exchangeRate: ExchangeRate): EmptyResult<DataError.Local> {
        return try {
            converterDao.upsertLocalExchangeRate(exchangeRate.toEntity())
            Result.Success(Unit)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteLocalExchangeRate(exchangeRate: ExchangeRate) {
        converterDao.deleteLocalExchangeRate(exchangeRate.toEntity())
    }

    override suspend fun clearLocalExchangeRates() {
        converterDao.clearLocalExchangeRates()
    }

    override fun observeCurrencyMetadata(): Flow<List<CurrencyMetadata>> {
        return converterDao.observeCurrencyMetadata().map { currencyMetaDataEntities ->
            currencyMetaDataEntities.map { it.toDomain() }
        }
    }

    override fun observeFilteredCurrencyMetaData(query: String): Flow<List<CurrencyMetadata>> {
        return converterDao.observeFilteredCurrencyMetadata(query).map { currencyMetaDataEntities ->
            currencyMetaDataEntities.map { it.toDomain() }
        }
    }

    override suspend fun upsertLocalCurrencyMetaData(
        currencyMetaData: CurrencyMetadata,
        rate: Double,
    ): EmptyResult<DataError.Local> {
        return try {
            converterDao.upsertLocalCurrencyMetadata(currencyMetaData.toEntity(rate))
            Result.Success(Unit)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun upsertLocalCurrencyMetaDataList(currencyMetadataList: List<CurrencyMetadata>): EmptyResult<DataError.Local> {
        return try {
            converterDao.upsertLocalCurrencyMetadataList(currencyMetadataList.map { it.toEntity() })
            Result.Success(Unit)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun clearLocalCurrencyMetadata() {
        converterDao.clearLocalCurrencyMetadata()
    }
}