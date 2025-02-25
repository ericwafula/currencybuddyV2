package tech.ericwathome.core.database

import android.database.sqlite.SQLiteFullException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tech.ericwathome.core.database.dao.ConverterDao
import tech.ericwathome.core.database.mappers.toDomain
import tech.ericwathome.core.database.mappers.toEntity
import tech.ericwathome.core.domain.converter.LocalConverterDataSource
import tech.ericwathome.core.domain.converter.model.CurrencyDetails
import tech.ericwathome.core.domain.converter.model.ExchangeRate
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.EmptyResult
import tech.ericwathome.core.domain.util.Result

class RoomLocalConverterDataSource(
    private val converterDao: ConverterDao,
) : LocalConverterDataSource {
    override fun observeSelectedExchangeRate(): Flow<ExchangeRate> {
        return converterDao.observeSelectedExchangeRate().map { it.toDomain() }
    }

    override fun observeNonSelectedExchangeRates(): Flow<List<ExchangeRate>> {
        return converterDao.observeNonSelectedExchangeRates().map { exchangeRateEntities ->
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

    override fun observeCurrencyDetails(): Flow<List<CurrencyDetails>> {
        return converterDao.observeCurrencyDetails().map { currencyDetailsEntities ->
            currencyDetailsEntities.map { it.toDomain() }
        }
    }

    override fun observeFilteredCurrencyDetails(query: String): Flow<List<CurrencyDetails>> {
        return converterDao.observeFilteredCurrencyDetails(query).map { currencyDetailsEntities ->
            currencyDetailsEntities.map { it.toDomain() }
        }
    }

    override suspend fun upsertLocalCurrencyDetails(
        currencyDetails: CurrencyDetails,
        rate: Double,
    ): EmptyResult<DataError.Local> {
        return try {
            converterDao.upsertLocalCurrencyDetails(currencyDetails.toEntity(rate))
            Result.Success(Unit)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun upsertLocalCurrencyDetailsList(currencyDetailsList: List<CurrencyDetails>): EmptyResult<DataError.Local> {
        return try {
            converterDao.upsertLocalCurrencyDetailsList(currencyDetailsList.map { it.toEntity() })
            Result.Success(Unit)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun clearLocalCurrencyDetails() {
        converterDao.clearLocalCurrencyDetails()
    }
}