package tech.ericwathome.converter.presentation

import tech.ericwathome.core.presentation.ui.UiText

sealed interface ConverterEvent {
    data class ShowToast(val message: UiText) : ConverterEvent

    data class ShowSnackbar(val message: UiText, val actionLabel: UiText) : ConverterEvent

    data object DismissSnackbar : ConverterEvent
}