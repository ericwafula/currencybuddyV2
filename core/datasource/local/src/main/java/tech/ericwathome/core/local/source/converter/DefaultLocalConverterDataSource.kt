package tech.ericwathome.core.local.source.converter

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import tech.ericwathome.core.domain.converter.LocalConverterDataSource
import tech.ericwathome.core.domain.converter.model.CurrencyMetadata
import tech.ericwathome.core.domain.converter.model.ExchangeRate
import tech.ericwathome.core.domain.util.DataError
import tech.ericwathome.core.domain.util.EmptyResult
import tech.ericwathome.core.local.model.mappers.toDomain
import tech.ericwathome.core.local.model.mappers.toEntity
import tech.ericwathome.core.local.utils.safeTransaction

internal class DefaultLocalConverterDataSource(
    private val converterDao: ConverterDao,
    private val converterPreferences: ConverterPreferences,
) : LocalConverterDataSource {
    override val defaultExchangeRateObservable: Flow<ExchangeRate>
        get() = converterDao.observeDefaultExchangeRate().filterNotNull().map { it.toDomain() }

    override val nonDefaultExchangeRatesObservable: Flow<List<ExchangeRate>>
        get() =
            converterDao.observeNonDefaultExchangeRates().map { exchangeRateEntities ->
                exchangeRateEntities.map { it.toDomain() }
            }
    override val lastMetadataSyncTimestamp: Flow<Long?>
        get() = converterPreferences.lastMetadataSyncTimestamp

    override val lastExchangeRateSyncTimestamp: Flow<Long?>
        get() = converterPreferences.lastExchangeRateSyncTimestamp

    override val isMetadataSyncingObservable: Flow<Boolean?>
        get() = converterPreferences.isMetadataSyncing

    override val isExchangeRateSyncingObservable: Flow<Boolean?>
        get() = converterPreferences.isExchangeRateSyncing

    override suspend fun setLastMetadataSyncTimestamp(value: Long) {
        converterPreferences.setLastMetadataSyncTimestamp(value)
    }

    override suspend fun setLastExchangeRateSyncTimestamp(value: Long) {
        converterPreferences.setLastExchangeRateSyncTimestamp(value)
    }

    override suspend fun setIsMetadataSyncing(value: Boolean) {
        converterPreferences.setIsMetadataSyncing(value)
    }

    override suspend fun setIsExchangeRateSyncing(value: Boolean) {
        converterPreferences.setIsExchangeRateSyncing(value)
    }

    override suspend fun retrieveSavedExchangeRates(): List<ExchangeRate> {
        return converterDao.retrieveSavedExchangeRates().map { it.toDomain() }
    }

    override suspend fun upsertLocalExchangeRate(exchangeRate: ExchangeRate): EmptyResult<DataError.Local> {
        return safeTransaction {
            converterDao.upsertLocalExchangeRate(exchangeRate.toEntity())
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
        return safeTransaction {
            converterDao.upsertLocalCurrencyMetadata(currencyMetaData.toEntity(rate))
        }
    }

    override suspend fun upsertLocalCurrencyMetaDataList(currencyMetadataList: List<CurrencyMetadata>): EmptyResult<DataError.Local> {
        return safeTransaction {
            converterDao.upsertLocalCurrencyMetadataList(currencyMetadataList.map { it.toEntity() })
        }
    }

    override suspend fun clearLocalCurrencyMetadata() {
        converterDao.clearLocalCurrencyMetadata()
    }
}