package tech.ericwathome.converter.presentation

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.ericwathome.core.domain.ConnectionObserver
import tech.ericwathome.core.domain.converter.ConverterRepository
import tech.ericwathome.core.domain.util.Result
import tech.ericwathome.core.presentation.ui.UiText
import tech.ericwathome.core.presentation.ui.asUiText

@Keep
@OptIn(FlowPreview::class)
class ConverterViewModel(
    private val converterRepository: ConverterRepository,
    private val connectionObserver: ConnectionObserver,
) : ViewModel() {
    private val _event = Channel<ConverterEvent>()
    val event = _event.receiveAsFlow()

    private val _state = MutableStateFlow(ConverterState())
    val state =
        _state.onStart {
            fetchExchangeRate()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ConverterState(),
        )
    private val maxIntegerDigits = 10
    private val maxFractionDigits = 2

    init {
        converterRepository
            .defaultExchangeRateObservable
            .onEach { exchangeRate ->
                _state.update { it.copy(result = exchangeRate.conversionResult.toString()) }
            }.launchIn(viewModelScope)

        converterRepository
            .currencyMetadataObservable
            .combine(connectionObserver.hasNetworkConnection.debounce(500)) { currencyMetadata, hasNetworkConnection ->
                if (currencyMetadata.isEmpty() && hasNetworkConnection) {
                    syncCurrencyMetadata()
                }

                val baseIndex =
                    currencyMetadata
                        .binarySearch { it.code.uppercase().compareTo(state.value.baseCurrencyCode.uppercase()) }

                val quoteIndex =
                    currencyMetadata
                        .binarySearch { it.code.uppercase().compareTo(state.value.quoteCurrencyCode.uppercase()) }

                val initialBaseFlagUrl =
                    currencyMetadata
                        .getOrNull(baseIndex)
                        ?.flag?.svg.orEmpty()

                val initialQuoteFlagUrl =
                    currencyMetadata
                        .getOrNull(quoteIndex)
                        ?.flag?.svg.orEmpty()

                _state.update {
                    it.copy(
                        currencyMetadataList = currencyMetadata,
                        baseFlagUrl = initialBaseFlagUrl,
                        quoteFlagUrl = initialQuoteFlagUrl,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: ConverterAction) {
        when (action) {
            is ConverterAction.OnEnterInput -> onEnterInput(action.input)
            ConverterAction.OnClickConvert -> fetchExchangeRate()
            ConverterAction.OnDeleteInput -> onDeleteInput()
            ConverterAction.OnClearInput -> onClearInput()
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

    private fun fetchExchangeRate() {
        viewModelScope.launch {
            val rawAmount = state.value.amount
            val processedAmount = if (rawAmount.endsWith(".")) "${rawAmount}0" else rawAmount

            if (rawAmount == "0") {
                _event.send(ConverterEvent.ShowToast(UiText.StringResource(R.string.enter_value_greater_than_zero)))

                return@launch
            }

            _state.update { it.copy(converting = true) }
            when (
                val result =
                    converterRepository.fetchExchangeRate(
                        fromCurrencyCode = state.value.baseCurrencyCode.lowercase(),
                        toCurrencyCode = state.value.quoteCurrencyCode.lowercase(),
                        amount = processedAmount.toDoubleOrNull() ?: 0.0,
                        isDefault = true,
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
}