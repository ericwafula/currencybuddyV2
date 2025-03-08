@file:Keep

package tech.ericwathome.core.presentation.ui

import androidx.annotation.Keep
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow

fun TextFieldState.textAsFlow() = snapshotFlow { text }