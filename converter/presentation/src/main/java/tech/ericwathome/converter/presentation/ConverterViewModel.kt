package tech.ericwathome.converter.presentation

import androidx.lifecycle.ViewModel
import tech.ericwathome.core.domain.converter.ConverterRepository

class ConverterViewModel(
    private val converterRepository: ConverterRepository,
) : ViewModel()