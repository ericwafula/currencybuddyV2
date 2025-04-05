package tech.ericwathome.converter.presentation

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.ericwathome.core.domain.ConnectionObserver
import tech.ericwathome.core.domain.converter.ConverterRepository
import tech.ericwathome.core.domain.converter.ConverterScheduler
import tech.ericwathome.core.domain.converter.LocalConverterDataSource
import tech.ericwathome.core.domain.converter.model.CurrencyMetadata
import tech.ericwathome.core.domain.util.Result
import tech.ericwathome.core.presentation.ui.UiText
import tech.ericwathome.core.presentation.ui.asUiText
import tech.ericwathome.core.presentation.ui.extract
import tech.ericwathome.core.presentation.ui.extractTriple
import kotlin.time.Duration.Companion.minutes

@Keep
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class ConverterViewModel(
    private val converterRepository: ConverterRepository,
    private val converterScheduler: ConverterScheduler,
    private val localConverterDataSource: LocalConverterDataSource,
    connectionObserver: ConnectionObserver,
) : ViewModel() {
    private val _event = Channel<ConverterEvent>()
    val event = _event.receiveAsFlow()

    private val maxIntegerDigits = 10
    private val maxFractionDigits = 2

    private val hasAcceptedNotificationPermission = MutableStateFlow(false)
    private val hasAcceptedLocationPermission = MutableStateFlow(false)

    private val _state = MutableStateFlow(ConverterState())
    val state =
        _state.onStart {
            initStateObservers()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ConverterState(),
        )

    private val shouldRetryFetchingExchangeRates =
        state
            .extract { it.isError }
            .combine(connectionObserver.networkStatus) { isError, isAvailable ->
                isError && isAvailable
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = false,
            )

    fun onAction(action: ConverterAction) {
        when (action) {
            is ConverterAction.OnEnterInput -> onEnterInput(action.input)
            ConverterAction.OnClickConvert -> viewModelScope.launch { fetchExchangeRate() }
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
            ConverterAction.OnClickSwapButton -> swapCurrencyPair()
            is ConverterAction.SubmitNotificationPermissionInfo ->
                handleNotificationPermissionInfo(
                    permissionGranted = action.permissionGranted,
                    showNotificationRationale = action.showNotificationRationale,
                )

            is ConverterAction.SubmitLocationPermissionInfo -> Unit

            else -> Unit
        }
    }

    private fun handleNotificationPermissionInfo(
        permissionGranted: Boolean,
        showNotificationRationale: Boolean,
    ) {
        _state.update { it.copy(showNotificationRationale = showNotificationRationale) }
        hasAcceptedNotificationPermission.value = permissionGranted
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
            )
        }
    }

    private fun showBottomSheet(isBaseCurrency: Boolean) {
        _state.update {
            it.copy(
                isBaseCurrencyToggled = isBaseCurrency,
                isQuoteCurrencyToggled = !isBaseCurrency,
                showCurrencyPickerBottomSheet = true,
            )
        }
    }

    private fun dismissBottomSheet() {
        _state.update {
            it.copy(
                showCurrencyPickerBottomSheet = false,
                isBaseCurrencyToggled = false,
                isQuoteCurrencyToggled = false,
                currentlySelectedBaseCurrencyMetadata = null,
                currentlySelectedQuoteCurrencyMetadata = null,
                filteredCurrencyMetadataList = state.value.filteredCurrencyMetadataList.map { metadata -> metadata.copy(isSelected = false) },
            )
        }
    }

    private fun onSelectCurrency(index: Int) {
        val updatedCurrencyDetailsList =
            state.value.filteredCurrencyMetadataList.mapIndexed { currencyMetadataIndex, currency ->
                currency.copy(isSelected = currencyMetadataIndex == index)
            }
        val selectedCurrency = state.value.filteredCurrencyMetadataList.getOrNull(index)

        _state.update {
            if (it.isBaseCurrencyToggled) {
                it.copy(currentlySelectedBaseCurrencyMetadata = selectedCurrency, filteredCurrencyMetadataList = updatedCurrencyDetailsList)
            } else {
                it.copy(currentlySelectedQuoteCurrencyMetadata = selectedCurrency, filteredCurrencyMetadataList = updatedCurrencyDetailsList)
            }
        }
    }

    private fun setSelectedBaseAndQuoteCurrencyCodesAndFlags() {
        val baseCurrencyCode = state.value.baseCurrencyCode
        val quoteCurrencyCode = state.value.quoteCurrencyCode
        val baseFlagUrlSvg = state.value.baseFlagUrlSvg
        val baseFlagUrlPng = state.value.baseFlagUrlSvg
        val quoteFlagUrlSvg = state.value.quoteFlagUrlSvg
        val quoteFlagUrlPng = state.value.quoteFlagUrlSvg

        _state.update {
            it.copy(
                baseCurrencyCode = state.value.currentlySelectedBaseCurrencyMetadata?.code ?: baseCurrencyCode,
                quoteCurrencyCode = state.value.currentlySelectedQuoteCurrencyMetadata?.code ?: quoteCurrencyCode,
                baseFlagUrlSvg = state.value.currentlySelectedBaseCurrencyMetadata?.flag?.svg ?: baseFlagUrlSvg,
                baseFlagUrlPng = state.value.currentlySelectedBaseCurrencyMetadata?.flag?.png ?: baseFlagUrlPng,
                quoteFlagUrlSvg = state.value.currentlySelectedQuoteCurrencyMetadata?.flag?.svg ?: quoteFlagUrlSvg,
                quoteFlagUrlPng = state.value.currentlySelectedQuoteCurrencyMetadata?.flag?.png ?: quoteFlagUrlPng,
            )
        }

        _state.update {
            it.copy(
                showCurrencyPickerBottomSheet = false,
                isBaseCurrencyToggled = false,
                isQuoteCurrencyToggled = false,
                filteredCurrencyMetadataList =
                    state.value.filteredCurrencyMetadataList.map { currencyMetadata ->
                        currencyMetadata.copy(isSelected = false)
                    },
            )
        }
    }

    private fun swapCurrencyPair() {
        val baseCode = state.value.baseCurrencyCode
        val baseFlagPng = state.value.baseFlagUrlPng
        val baseFlagSvg = state.value.baseFlagUrlSvg
        val quoteCode = state.value.quoteCurrencyCode
        val quoteFlagPng = state.value.quoteFlagUrlPng
        val quoteFlagSvg = state.value.quoteFlagUrlSvg

        _state.update {
            it.copy(
                baseCurrencyCode = quoteCode,
                baseFlagUrlPng = quoteFlagPng,
                baseFlagUrlSvg = quoteFlagSvg,
                quoteCurrencyCode = baseCode,
                quoteFlagUrlPng = baseFlagPng,
                quoteFlagUrlSvg = baseFlagSvg,
            )
        }
    }

    private fun initStateObservers() {
        observeNotificationPermission()
        initializeDefaultCurrencyPair()
        observeExchangeRate()
        observeExchangeRateTriggers()
        observeSearchQuery()
        observeCurrencyMetadataAndUpdateFlags()
        observeSyncStates()
        observeCanContinueState()
        observeRetryOnConnection()
    }

    private fun observeNotificationPermission() {
        hasAcceptedNotificationPermission
            .onEach { hasAcceptedNotificationPermission ->
                localConverterDataSource.setHasNotificationPermission(hasAcceptedNotificationPermission)
            }.launchIn(viewModelScope)
    }

    private fun initializeDefaultCurrencyPair() {
        viewModelScope.launch {
            val initialExchangeRateJob =
                launch {
                    val defaultExchangeRate = converterRepository.exchangeRateObservable.firstOrNull()

                    _state.update {
                        it.copy(
                            baseCurrencyCode = defaultExchangeRate?.baseCode?.uppercase() ?: "EUR",
                            quoteCurrencyCode = defaultExchangeRate?.targetCode?.uppercase() ?: "USD",
                        )
                    }
                }

            initialExchangeRateJob.join()
            scheduleExchangeRatesSync()
        }
    }

    private fun observeExchangeRate() {
        converterRepository
            .exchangeRateObservable
            .onEach { exchangeRate ->
                _state.update { it.copy(result = exchangeRate?.conversionResult?.toString() ?: "0.0") }
            }.launchIn(viewModelScope)
    }

    private fun observeExchangeRateTriggers() {
        viewModelScope.launch {
            state
                .extractTriple { Triple(it.amount, it.baseCurrencyCode, it.quoteCurrencyCode) }
                .debounce(500)
                .collectLatest { fetchExchangeRate() }
        }
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            state
                .extract {
                    it.searchQuery
                }
                .onEach {
                    _state.update { it.copy(isSearching = true) }
                }
                .debounce(500)
                .flatMapLatest { searchQuery ->
                    converterRepository.observeFilteredCurrencyMetaData(searchQuery.trim())
                }
                .collectLatest { filteredCurrencyMetadata ->
                    _state.update {
                        it.copy(
                            filteredCurrencyMetadataList = filteredCurrencyMetadata,
                            isSearching = false,
                        )
                    }
                }
        }
    }

    private fun observeCurrencyMetadataAndUpdateFlags() {
        converterRepository.currencyMetadataObservable
            .onEach { metadataList ->
                val (svgs, pngs) =
                    getCurrencyFlagUrls(
                        baseCode = state.value.baseCurrencyCode,
                        quoteCode = state.value.quoteCurrencyCode,
                        currencyList = metadataList,
                    )

                val (baseFlagSvg, quoteFlagSvg) = svgs
                val (baseFlagPng, quoteFlagPng) = pngs

                _state.update {
                    it.copy(
                        unfilteredCurrencyMetadataList = metadataList,
                        baseFlagUrlPng = baseFlagPng,
                        quoteFlagUrlPng = quoteFlagPng,
                        baseFlagUrlSvg = baseFlagSvg,
                        quoteFlagUrlSvg = quoteFlagSvg,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getCurrencyFlagUrls(
        baseCode: String,
        quoteCode: String,
        currencyList: List<CurrencyMetadata>,
    ): Pair<Pair<String, String>, Pair<String, String>> {
        val sorted = currencyList.sortedBy { it.code }
        val baseIndex = sorted.binarySearch { it.code.uppercase().compareTo(baseCode.uppercase()) }
        val quoteIndex = sorted.binarySearch { it.code.uppercase().compareTo(quoteCode.uppercase()) }

        val baseFlagSvg = sorted.getOrNull(baseIndex)?.flag?.svg.orEmpty()
        val baseFlagPng = sorted.getOrNull(baseIndex)?.flag?.png.orEmpty()
        val quoteFlagSvg = sorted.getOrNull(quoteIndex)?.flag?.svg.orEmpty()
        val quoteFlagPng = sorted.getOrNull(quoteIndex)?.flag?.png.orEmpty()

        return (baseFlagSvg to quoteFlagSvg) to (baseFlagPng to quoteFlagPng)
    }

    private fun observeSyncStates() {
        converterRepository.isMetadataSyncingObservable
            .filterNotNull()
            .onEach { isSyncing ->
                _state.update { it.copy(isSyncingCurrencies = isSyncing) }
            }
            .launchIn(viewModelScope)

        converterRepository.isExchangeRateSyncingObservable
            .filterNotNull()
            .onEach { isSyncing ->
                _state.update { it.copy(isSyncingConversionRates = isSyncing) }
            }
            .launchIn(viewModelScope)
    }

    private fun observeCanContinueState() {
        state
            .extractTriple {
                Triple(
                    it.isBaseCurrencyToggled,
                    it.currentlySelectedBaseCurrencyMetadata,
                    it.currentlySelectedQuoteCurrencyMetadata,
                )
            }
            .map { (isBase, baseMeta, quoteMeta) ->
                if (isBase) baseMeta != null else quoteMeta != null
            }
            .onEach { canSubmit ->
                _state.update {
                    it.copy(
                        canContinue = canSubmit,
                    )
                }
            }.launchIn(viewModelScope)
    }

    private fun observeRetryOnConnection() {
        shouldRetryFetchingExchangeRates
            .onEach { shouldRetryFetchingExchangeRates ->
                if (shouldRetryFetchingExchangeRates) {
                    fetchExchangeRate()
                }
            }
            .launchIn(viewModelScope)
    }

    private fun scheduleExchangeRatesSync() {
        viewModelScope.launch {
            converterScheduler.scheduleSync(
                ConverterScheduler.SyncType.FetchExchangeRate(
                    duration = 30.minutes,
                    withInitialDelay = false,
                ),
            )
        }
    }

    private suspend fun fetchExchangeRate() {
        val rawAmount = state.value.amount
        val processedAmount = if (rawAmount.endsWith(".")) "${rawAmount}0" else rawAmount

        _state.update { it.copy(isSyncingConversionRates = true) }
        when (
            val result =
                converterRepository.fetchExchangeRate(
                    fromCurrencyCode = state.value.baseCurrencyCode.lowercase(),
                    toCurrencyCode = state.value.quoteCurrencyCode.lowercase(),
                    amount = processedAmount.toDoubleOrNull() ?: 0.0,
                    baseFlagPng = state.value.baseFlagUrlPng,
                    baseFlagSvg = state.value.baseFlagUrlSvg,
                    quoteFlagPng = state.value.quoteFlagUrlPng,
                    quoteFlagSvg = state.value.quoteFlagUrlSvg,
                )
        ) {
            is Result.Error -> {
                _state.update {
                    it.copy(
                        isSyncingConversionRates = false,
                        isError = true,
                        errorMessage = result.error.asUiText(),
                    )
                }
            }

            is Result.Success -> {
                _state.update {
                    it.copy(
                        isSyncingConversionRates = false,
                        isError = false,
                        errorMessage = null,
                    )
                }
            }
        }
    }

    private fun syncCurrencyMetadata() {
        _state.update { it.copy(isSyncingCurrencies = true) }
        viewModelScope.launch {
            when (val result = converterRepository.syncCurrencyMetadata()) {
                is Result.Error -> {
                    _state.update { it.copy(isSyncingCurrencies = false) }
                    _event.send(ConverterEvent.ShowToast(result.error.asUiText()))
                }

                is Result.Success -> {
                    _state.update { it.copy(isSyncingCurrencies = false) }
                    fetchExchangeRate()
                    _event.send(ConverterEvent.ShowToast(UiText.StringResource(R.string.currency_metadata_synced_successfully)))
                }
            }
        }
    }
}