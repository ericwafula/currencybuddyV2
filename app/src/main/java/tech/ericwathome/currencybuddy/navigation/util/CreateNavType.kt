package tech.ericwathome.currencybuddy.navigation.util

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.serialization.json.Json

inline fun <reified T : Parcelable> createNavType(isNullableAllowed: Boolean = false): NavType<T> {
    val json =
        Json {
            isLenient = true
            ignoreUnknownKeys = true
        }

    return object : NavType<T>(isNullableAllowed) {
        override fun get(
            bundle: Bundle,
            key: String,
        ): T? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(key, T::class.java)
            } else {
                bundle.getParcelable(key)
            }
        }

        override fun serializeAsValue(value: T): String {
            return json.encodeToString(value)
        }

        override fun parseValue(value: String): T {
            return json.decodeFromString(value)
        }

        override fun put(
            bundle: Bundle,
            key: String,
            value: T,
        ) {
            bundle.putParcelable(key, value)
        }
    }
}
