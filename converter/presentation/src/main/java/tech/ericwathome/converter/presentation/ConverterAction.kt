package tech.ericwathome.converter.presentation

sealed interface ConverterAction {
    data class OnClickInputKey(val input: Char) : ConverterAction
    data object OnClickConvert : ConverterAction
    data object OnClickDelete : ConverterAction
    data object OnClickClear : ConverterAction
    data object OnClickBaseButton : ConverterAction
    data object OnClickQuoteButton : ConverterAction
    data object OnClickSwapButton : ConverterAction
}