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
import kotlinx.coroutines.flow.emptyFlow
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
import tech.ericwathome.core.domain.ConverterScheduler
import tech.ericwathome.core.domain.SyncEventManager
import tech.ericwathome.core.domain.converter.ConverterRepository
import tech.ericwathome.core.domain.converter.model.CurrencyMetadata
import tech.ericwathome.core.domain.util.Result
import tech.ericwathome.core.notification.NotificationHandler
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
    private val syncEventManager: SyncEventManager,
    private val notificationHandler: NotificationHandler,
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
            handleSyncEventNotifications()
            scheduleExchangeRatesSync()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ConverterState(),
        )

    private val shouldRetryFetchingExchangeRates =
        state
            .extract { it.isError }
            .combine(connectionObserver.networkStatus) { isError, networkStatus ->
                isError && networkStatus == ConnectionObserver.NetworkStatus.Available
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = false,
            )

    private val syncEvent =
        hasAcceptedNotificationPermission
            .flatMapLatest { hasAcceptedNotificationPermission ->
                if (hasAcceptedNotificationPermission) {
                    syncEventManager.event
                } else {
                    emptyFlow()
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = null,
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

            is ConverterAction.SubmitLocationPermissionInfo ->
                handleLocationPermissionInfo(
                    permissionGranted = action.permissionGranted,
                    showLocationRationale = action.showLocationRationale,
                )

            else -> Unit
        }
    }

    private fun handleLocationPermissionInfo(
        permissionGranted: Boolean,
        showLocationRationale: Boolean,
    ) {
        _state.update { it.copy(showLocationRationale = showLocationRationale) }
        hasAcceptedLocationPermission.value = permissionGranted
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
        val baseFlag = state.value.baseFlagUrl
        val quoteCode = state.value.quoteCurrencyCode
        val quoteFlag = state.value.quoteFlagUrl

        _state.update {
            it.copy(
                baseCurrencyCode = quoteCode,
                baseFlagUrl = quoteFlag,
                quoteCurrencyCode = baseCode,
                quoteFlagUrl = baseFlag,
            )
        }
    }

    private fun initStateObservers() {
        initializeDefaultCurrencyPair()
        observeExchangeRate()
        observeExchangeRateTriggers()
        observeSearchQuery()
        observeCurrencyMetadataAndUpdateFlags()
        observeSyncStates()
        observeCanContinueState()
        observeRetryOnConnection()
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
            fetchExchangeRate()
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
                val (baseFlag, quoteFlag) =
                    getCurrencyFlagUrls(
                        baseCode = state.value.baseCurrencyCode,
                        quoteCode = state.value.quoteCurrencyCode,
                        currencyList = metadataList,
                    )

                _state.update {
                    it.copy(
                        unfilteredCurrencyMetadataList = metadataList,
                        baseFlagUrl = baseFlag,
                        quoteFlagUrl = quoteFlag,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getCurrencyFlagUrls(
        baseCode: String,
        quoteCode: String,
        currencyList: List<CurrencyMetadata>,
    ): Pair<String, String> {
        val sorted = currencyList.sortedBy { it.code }
        val baseIndex = sorted.binarySearch { it.code.uppercase().compareTo(baseCode.uppercase()) }
        val quoteIndex = sorted.binarySearch { it.code.uppercase().compareTo(quoteCode.uppercase()) }

        val baseFlag = sorted.getOrNull(baseIndex)?.flag?.svg.orEmpty()
        val quoteFlag = sorted.getOrNull(quoteIndex)?.flag?.svg.orEmpty()

        return baseFlag to quoteFlag
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
                    withInitialDelay = true,
                ),
            )
        }
    }

    private fun handleSyncEventNotifications() {
        syncEvent
            .filterNotNull()
            .onEach { syncEvent ->
                when (syncEvent) {
                    SyncEventManager.SyncEvent.SyncMetadataSuccess -> {
                        showSyncNotification(
                            title = UiText.StringResource(R.string.sync_complete),
                            message = UiText.StringResource(R.string.currency_sync_completed_successfully),
                        )
                    }

                    SyncEventManager.SyncEvent.SyncMetadataError -> {
                        showSyncNotification(
                            title = UiText.StringResource(R.string.sync_failure),
                            message = UiText.StringResource(R.string.currency_sync_failed),
                        )
                    }

                    SyncEventManager.SyncEvent.SyncSelectedCurrencyPairError -> {
                        showSyncNotification(
                            title = UiText.StringResource(R.string.sync_failure),
                            message = UiText.StringResource(R.string.exchange_rate_sync_failed),
                        )
                    }

                    SyncEventManager.SyncEvent.SyncSelectedCurrencyPairSuccess -> {
                        _state.update { it.copy(isError = false) }

                        showSyncNotification(
                            title = UiText.StringResource(R.string.sync_complete),
                            message = UiText.StringResource(R.string.exchange_rate_sync_completed_successfully),
                        )
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun showSyncNotification(
        title: UiText,
        message: UiText,
    ) {
        notificationHandler.showSimpleNotification(
            title = title,
            message = message,
            notificationType = NotificationHandler.NotificationType.Sync,
        )
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
                    baseFlag = state.value.baseFlagUrl,
                    quoteFlag = state.value.quoteFlagUrl,
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