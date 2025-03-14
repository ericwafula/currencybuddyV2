package tech.ericwathome.converter.presentation

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.ericwathome.core.domain.ConverterScheduler
import tech.ericwathome.core.domain.converter.ConverterRepository
import tech.ericwathome.core.domain.util.Result
import tech.ericwathome.core.presentation.ui.UiText
import tech.ericwathome.core.presentation.ui.asUiText
import timber.log.Timber
import kotlin.time.Duration.Companion.minutes

@Keep
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class ConverterViewModel(
    private val converterRepository: ConverterRepository,
    private val converterScheduler: ConverterScheduler,
) : ViewModel() {
    private val _event = Channel<ConverterEvent>()
    val event = _event.receiveAsFlow()

    private val maxIntegerDigits = 10
    private val maxFractionDigits = 2
    private val defaultExchangeRate = converterRepository.getSavedExchangeRate()

    private val defaultState =
        ConverterState(
            baseCurrencyCode = defaultExchangeRate?.baseCode?.uppercase() ?: "EUR",
            quoteCurrencyCode = defaultExchangeRate?.targetCode?.uppercase() ?: "USD",
            baseFlagUrl = defaultExchangeRate?.baseFlag ?: "",
            quoteFlagUrl = defaultExchangeRate?.targetFlag ?: "",
        )

    private val _state = MutableStateFlow(defaultState)
    val state =
        _state.onStart {
            initStateObservers()
            fetchExchangeRate()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = defaultState,
        )

    private val searchResults =
        state
            .debounce(500)
            .flatMapLatest { state ->
                converterRepository.observeFilteredCurrencyMetaData(state.searchQuery)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList(),
            )

    private val canSubmit =
        state.map { state ->
            if (state.isBaseCurrencyToggled) {
                state.currentlySelectedBaseCurrencyMetadata != null
            } else {
                state.currentlySelectedQuoteCurrencyMetadata != null
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = false,
        )

    fun onAction(action: ConverterAction) {
        when (action) {
            is ConverterAction.OnEnterInput -> onEnterInput(action.input)
            ConverterAction.OnClickConvert -> fetchExchangeRate()
            ConverterAction.OnDeleteInput -> onDeleteInput()
            ConverterAction.OnClearInput -> onClearInput()
            ConverterAction.OnRefresh -> syncCurrencyMetadata()
            ConverterAction.OnClickBaseButton -> showBottomSheet(isBaseCurrency = true)
            ConverterAction.OnClickQuoteButton -> showBottomSheet(isBaseCurrency = false)
            ConverterAction.OnDismissBottomSheet -> dismissBottomSheet()
            ConverterAction.OnClickContinue -> setSelectedBaseAndQuoteCurrencyCodesAndFlags()
            is ConverterAction.OnSelectCurrency -> onSelectCurrency(action.index)
            is ConverterAction.OnSelectQuoteCurrency -> onSelectCurrency(action.index)
            is ConverterAction.OnEnterSearchQuery -> onEnterSearchQuery(action.query)
            else -> Unit
        }
    }

    private fun onEnterInput(input: Char) {
        val currentInput = state.value.amount

        val newInput =
            when {
                input.isDigit() -> {
                    if (currentInput == "0") input.toString() else currentInput + input
                }

                input == '.' -> {
                    when {
                        currentInput.isEmpty() -> "0."
                        currentInput.contains('.') -> return
                        else -> currentInput + input
                    }
                }

                else -> return
            }

        val parts = newInput.split('.')
        val integerPart = parts[0]
        val fractionPart = if (parts.size > 1) parts[1] else ""

        if (integerPart.length > maxIntegerDigits) return

        if (fractionPart.length > maxFractionDigits) return

        _state.update { it.copy(amount = newInput) }
    }

    private fun onDeleteInput() {
        val currentInput = state.value.amount
        if (currentInput.isEmpty() || currentInput == "0") return

        val updatedInput = currentInput.dropLast(1).ifEmpty { "0" }
        _state.update { it.copy(amount = updatedInput) }
    }

    private fun onClearInput() {
        _state.update { it.copy(amount = "0") }
    }

    private fun onEnterSearchQuery(query: String) {
        _state.update {
            it.copy(
                searchQuery = query,
                isSearching = true,
            )
        }
    }

    private fun showBottomSheet(isBaseCurrency: Boolean) {
        _state.update {
            it.copy(
                isBaseCurrencyToggled = isBaseCurrency,
                showCurrencyPickerBottomSheet = true,
            )
        }
    }

    private fun dismissBottomSheet() {
        _state.update {
            it.copy(
                showCurrencyPickerBottomSheet = false,
                currentlySelectedBaseCurrencyMetadata = null,
                currentlySelectedQuoteCurrencyMetadata = null,
                currencyMetadataList = state.value.currencyMetadataList.map { metadata -> metadata.copy(isSelected = false) },
            )
        }
    }

    private fun onSelectCurrency(index: Int) {
        val updatedCurrencyDetailsList =
            state.value.currencyMetadataList.mapIndexed { currencyMetadataIndex, currency ->
                currency.copy(isSelected = currencyMetadataIndex == index)
            }
        val selectedCurrency = state.value.currencyMetadataList.getOrNull(index)

        _state.update {
            if (it.isBaseCurrencyToggled) {
                it.copy(currentlySelectedBaseCurrencyMetadata = selectedCurrency, currencyMetadataList = updatedCurrencyDetailsList)
            } else {
                it.copy(currentlySelectedQuoteCurrencyMetadata = selectedCurrency, currencyMetadataList = updatedCurrencyDetailsList)
            }
        }
    }

    private fun setSelectedBaseAndQuoteCurrencyCodesAndFlags() {
        val baseCurrencyCode = state.value.baseCurrencyCode
        val quoteCurrencyCode = state.value.quoteCurrencyCode
        val baseFlagUrl = state.value.baseFlagUrl
        val quoteFlagUrl = state.value.quoteFlagUrl

        _state.update {
            it.copy(
                baseCurrencyCode = state.value.currentlySelectedBaseCurrencyMetadata?.code ?: baseCurrencyCode,
                quoteCurrencyCode = state.value.currentlySelectedQuoteCurrencyMetadata?.code ?: quoteCurrencyCode,
                baseFlagUrl = state.value.currentlySelectedBaseCurrencyMetadata?.flag?.svg ?: baseFlagUrl,
                quoteFlagUrl = state.value.currentlySelectedQuoteCurrencyMetadata?.flag?.svg ?: quoteFlagUrl,
            )
        }

        _state.update {
            it.copy(
                showCurrencyPickerBottomSheet = false,
                currencyMetadataList =
                    state.value.currencyMetadataList.map { currencyMetadata ->
                        currencyMetadata.copy(isSelected = false)
                    },
            )
        }
    }

    private fun fetchExchangeRate() {
        viewModelScope.launch {
            val rawAmount = state.value.amount
            val processedAmount = if (rawAmount.endsWith(".")) "${rawAmount}0" else rawAmount

            _state.update { it.copy(converting = true) }
            when (
                val result =
                    converterRepository.fetchExchangeRate(
                        fromCurrencyCode = state.value.baseCurrencyCode.lowercase(),
                        toCurrencyCode = state.value.quoteCurrencyCode.lowercase(),
                        amount = processedAmount.toDoubleOrNull() ?: 0.0,
                        baseFlag = state.value.baseFlagUrl,
                        quoteFlag = state.value.quoteFlagUrl,
                    )
            ) {
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            converting = false,
                            isError = true,
                            errorMessage = result.error.asUiText(),
                        )
                    }
                }

                is Result.Success -> {
                    _state.update {
                        it.copy(
                            converting = false,
                            isError = false,
                            errorMessage = null,
                        )
                    }
                }
            }
        }
    }

    private fun syncCurrencyMetadata() {
        _state.update { it.copy(isSyncing = true) }
        viewModelScope.launch {
            when (val result = converterRepository.syncCurrencyMetadata()) {
                is Result.Error -> {
                    _state.update { it.copy(isSyncing = false) }
                    _event.send(ConverterEvent.ShowToast(result.error.asUiText()))
                }

                is Result.Success -> {
                    _state.update { it.copy(isSyncing = false) }
                    fetchExchangeRate()
                    _event.send(ConverterEvent.ShowToast(UiText.StringResource(R.string.currency_metadata_synced_successfully)))
                }
            }
        }
    }

    private fun initStateObservers() {
        converterRepository
            .exchangeRateObservable.onEach { exchangeRate ->
                Timber.tag("ConverterViewModel").d("Exchange rate: $exchangeRate")
                _state.update { it.copy(result = exchangeRate?.conversionResult?.toString() ?: "0.0") }
            }.launchIn(viewModelScope)

        combine(
            searchResults,
            converterRepository.currencyMetadataObservable,
        ) { filteredCurrencyMetadata, currencyMetadata ->
            val currencyMetadataSortedByCode = currencyMetadata.sortedBy { it.code }
            val baseCode = state.value.baseCurrencyCode.uppercase()
            val quoteCode = state.value.quoteCurrencyCode.uppercase()

            val baseIndex =
                currencyMetadataSortedByCode
                    .binarySearch { it.code.uppercase().compareTo(baseCode) }

            val quoteIndex =
                currencyMetadataSortedByCode
                    .binarySearch { it.code.uppercase().compareTo(quoteCode) }

            val initialBaseFlagUrl =
                currencyMetadataSortedByCode
                    .getOrNull(baseIndex)
                    ?.flag?.svg.orEmpty()

            val initialQuoteFlagUrl =
                currencyMetadataSortedByCode
                    .getOrNull(quoteIndex)
                    ?.flag?.svg.orEmpty()

            _state.update {
                it.copy(
                    currencyMetadataList = filteredCurrencyMetadata,
                    baseFlagUrl = initialBaseFlagUrl,
                    quoteFlagUrl = initialQuoteFlagUrl,
                    isSearching = false,
                )
            }
        }.launchIn(viewModelScope)

        converterRepository.isMetadataSyncingObservable
            .filterNotNull()
            .onEach { isSyncing ->
                _state.update { it.copy(isSyncing = isSyncing) }
            }
            .launchIn(viewModelScope)

        converterRepository.isExchangeRateSyncingObservable
            .filterNotNull()
            .onEach { isSyncing ->
                _state.update { it.copy(converting = isSyncing) }
            }
            .launchIn(viewModelScope)

        canSubmit.onEach { canSubmit ->
            _state.update {
                it.copy(
                    canContinue = canSubmit,
                )
            }
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            converterScheduler.scheduleSync(ConverterScheduler.SyncType.FetchExchangeRates(30.minutes))
        }
    }
}