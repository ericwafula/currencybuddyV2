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
    override fun getSelectedSavedExchangeRate(): Flow<ExchangeRate> {
        return converterDao.getSelectedSavedExchangeRate().map { it.toDomain() }
    }

    override fun getSavedExchangeRates(): Flow<List<ExchangeRate>> {
        return converterDao.getSavedExchangeRates().map { exchangeRateEntities ->
            exchangeRateEntities.map { it.toDomain() }
        }
    }

    override suspend fun getSavedExchangedRatesList(): List<ExchangeRate> {
        return converterDao.getSavedExchangedRatesList().map { it.toDomain() }
    }

    override suspend fun upsertToSavedExchangeRates(exchangeRate: ExchangeRate): EmptyResult<DataError.Local> {
        return try {
            converterDao.upsertToSavedExchangeRates(exchangeRate.toEntity())
            Result.Success(Unit)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun removeFromSavedExchangeRates(exchangeRate: ExchangeRate) {
        converterDao.removeFromSavedExchangeRates(exchangeRate.toEntity())
    }

    override suspend fun clearAllSavedExchangeRates() {
        converterDao.clearAllSavedExchangeRates()
    }

    override fun getCurrencyDetails(): Flow<List<CurrencyDetails>> {
        return converterDao.getCurrencyDetails().map { currencyDetailsEntities ->
            currencyDetailsEntities.map { it.toDomain() }
        }
    }

    override fun queryCurrencyDetails(query: String): Flow<List<CurrencyDetails>> {
        return converterDao.queryCurrencyDetails(query).map { currencyDetailsEntities ->
            currencyDetailsEntities.map { it.toDomain() }
        }
    }

    override suspend fun upsertCurrencyDetails(
        currencyDetails: CurrencyDetails,
        rate: Double,
    ): EmptyResult<DataError.Local> {
        return try {
            converterDao.upsertCurrencyDetails(currencyDetails.toEntity(rate))
            Result.Success(Unit)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun upsertCurrencyDetailsList(currencyDetailsList: List<CurrencyDetails>): EmptyResult<DataError.Local> {
        return try {
            converterDao.upsertCurrencyDetailsList(currencyDetailsList.map { it.toEntity() })
            Result.Success(Unit)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun clearAllCurrencyDetails() {
        converterDao.clearAllCurrencyDetails()
    }
}