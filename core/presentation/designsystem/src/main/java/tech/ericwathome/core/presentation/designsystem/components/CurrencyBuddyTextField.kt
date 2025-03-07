package tech.ericwathome.core.presentation.designsystem.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.ericwathome.core.presentation.designsystem.CurrencybuddyTheme
import tech.ericwathome.core.presentation.designsystem.assets.SearchIcon
import tech.ericwathome.core.presentation.designsystem.utils.PreviewLightDarkWithBackground

@Composable
fun CurrencyBuddyTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    hint: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onLeadingIconClick: (() -> Unit)? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        modifier =
            modifier
                .onFocusEvent { event -> isFocused = event.isFocused }
                .height(56.dp),
        state = state,
        enabled = enabled,
        lineLimits = TextFieldLineLimits.SingleLine,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
        readOnly = readOnly,
        keyboardOptions =
            KeyboardOptions(
                keyboardType = keyboardType,
            ),
        textStyle =
            LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
            ),
        decorator = { innerTextField ->
            Box(
                modifier =
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            width = 1.dp,
                            color =
                                if (isFocused) {
                                    MaterialTheme.colorScheme.onSurface
                                } else {
                                    MaterialTheme.colorScheme.onSurface.copy(0.1f)
                                },
                            shape = RoundedCornerShape(12.dp),
                        ),
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    leadingIcon?.let {
                        IconButton(
                            onClick = { onLeadingIconClick?.invoke() },
                        ) {
                            Icon(imageVector = leadingIcon, contentDescription = null)
                        }
                    }
                    Box(
                        modifier =
                            Modifier
                                .weight(1f)
                                .then(
                                    if (leadingIcon == null) {
                                        Modifier.padding(start = 10.dp)
                                    } else {
                                        Modifier
                                    },
                                )
                                .then(
                                    if (trailingIcon == null) {
                                        Modifier.padding(end = 10.dp)
                                    } else {
                                        Modifier
                                    },
                                ),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        if (state.text.isBlank() && !isFocused) {
                            Text(
                                text = hint,
                                style =
                                    MaterialTheme.typography.bodySmall.copy(
                                        color =
                                            if (isFocused) {
                                                MaterialTheme.colorScheme.onSurface
                                            } else {
                                                MaterialTheme.colorScheme.onSurface.copy(0.5f)
                                            },
                                    ),
                            )
                        }
                        innerTextField()
                    }
                    trailingIcon?.let {
                        IconButton(
                            onClick = { onTrailingIconClick?.invoke() },
                        ) {
                            Icon(imageVector = trailingIcon, contentDescription = null)
                        }
                    }
                }
            }
        },
    )
}

@PreviewLightDarkWithBackground
@Composable
private fun SearchTextFieldPreview() {
    CurrencybuddyTheme {
        Surface {
            Box(modifier = Modifier.padding(16.dp)) {
                CurrencyBuddyTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = TextFieldState("Hello"),
                    leadingIcon = SearchIcon,
                    hint = "Search currency...",
                )
            }
        }
    }
}