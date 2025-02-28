@file:Keep

package tech.ericwathome.core.presentation.designsystem.utils

import android.content.res.Configuration
import androidx.annotation.Keep
import androidx.compose.ui.tooling.preview.Preview

@Keep
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
annotation class PreviewLightDarkWithBackground

@Keep
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, showSystemUi = true)
annotation class PreviewLightDarkWithBackgroundAndSystemUi