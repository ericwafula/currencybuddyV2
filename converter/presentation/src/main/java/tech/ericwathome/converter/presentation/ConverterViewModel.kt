package tech.ericwathome.converter.presentation

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.ericwathome.core.domain.converter.ConverterRepository
import tech.ericwathome.core.domain.util.Result
import tech.ericwathome.core.presentation.ui.asUiText
import timber.log.Timber

@Keep
class ConverterViewModel(
    private val converterRepository: ConverterRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ConverterState())
    val state =
        _state.onStart {
            getExchangeRate()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ConverterState(),
        )

    init {
        converterRepository
            .observeDefaultExchangeRate()
            .onEach { exchangeRate ->
                _state.update { it.copy(result = exchangeRate.conversionResult.toString()) }
            }.launchIn(viewModelScope)
    }

    fun onAction(action: ConverterAction) {
        when (action) {
            is ConverterAction.OnEnterInput -> onEnterInput(action.input)
            ConverterAction.OnClickConvert -> getExchangeRate()
            ConverterAction.OnDeleteInput -> onDeleteInput()
            ConverterAction.OnClearInput -> onClearInput()
            else -> Unit
        }
    }

    private fun onEnterInput(input: Char) {
        var currentInput = state.value.amount

        Timber.tag("ConverterViewModel").d("currentInput: $currentInput, newInput: $input")
        when {
            input.isDigit() -> {
                currentInput = if (currentInput == "0") input.toString() else currentInput + input
            }

            input == '.' -> {
                when {
                    currentInput.isEmpty() -> currentInput += "0."
                    currentInput.contains('.') -> return
                    else -> currentInput += input
                }
            }

            else -> return
        }
        _state.update { it.copy(amount = currentInput) }
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

    private fun getExchangeRate() {
        _state.update { it.copy(converting = true) }
        viewModelScope.launch {
            val rawAmount = state.value.amount
            val processedAmount = if (rawAmount.endsWith(".")) "${rawAmount}0" else rawAmount

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
}