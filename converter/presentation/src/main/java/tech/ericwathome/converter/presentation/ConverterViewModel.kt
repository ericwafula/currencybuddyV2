package tech.ericwathome.converter.presentation

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.ericwathome.core.domain.converter.ConverterRepository
import tech.ericwathome.core.domain.util.Result
import tech.ericwathome.core.presentation.ui.asUiText

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
        when {
            input.isDigit() -> {
                currentInput += input
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
        if (currentInput.isEmpty()) return

        val updatedInput = currentInput.dropLast(1)
        _state.update { it.copy(amount = updatedInput) }
    }

    private fun onClearInput() {
        _state.update { it.copy(amount = "") }
    }

    private fun getExchangeRate() {
        _state.update { it.copy(converting = true) }
        viewModelScope.launch {
            when (
                val result =
                    converterRepository.fetchExchangeRate(
                        fromCurrencyCode = state.value.baseCurrencyCode,
                        toCurrencyCode = state.value.quoteCurrencyCode,
                        amount = state.value.amount.toDoubleOrNull() ?: 0.0,
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
                            result = result.data.toString(),
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