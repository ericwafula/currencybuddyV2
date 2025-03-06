package tech.ericwathome.converter.presentation

sealed interface ConverterAction {
    data class OnEnterInput(val input: Char) : ConverterAction

    data object OnClickConvert : ConverterAction

    data object OnDeleteInput : ConverterAction

    data object OnClearInput : ConverterAction

    data object OnClickBaseButton : ConverterAction

    data object OnClickQuoteButton : ConverterAction

    data object OnClickSwapButton : ConverterAction

    data object OnRefresh : ConverterAction
}